package rocks.waffle.telekt.network

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.call
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.url
import io.ktor.client.response.readBytes
import io.ktor.client.response.readText
import io.ktor.http.*
import io.ktor.http.content.TextContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.io.streams.asInput
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonPrimitive
import mu.KotlinLogging
import rocks.waffle.telekt.exceptions.*
import rocks.waffle.telekt.network.requests.abstracts.MultipartRequest
import rocks.waffle.telekt.network.requests.abstracts.Request
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.util.serializer
import java.nio.file.Files
import java.nio.file.Path

//<editor-fold desc="response classes">
/**
 * Represents telegram answer
 *
 * More: https://core.telegram.org/bots/api#making-requests
 */
@Serializable data class TResponse<T>(
    val ok: Boolean,
    val result: T? = null,
    @SerialName("description") val errorDescription: String? = null,
    @SerialName("error_code") val errorCode: Int? = null,
    val parameters: ResponseParameters? = null
)

/**
 * Contains information about why a request was unsuccessful.
 */
@Serializable data class ResponseParameters(
    @SerialName("migrate_to_chat_id") val migrateCoChatId: Long? = null,
    @SerialName("retry_after") val retryAfter: Int? = null
)
//</editor-fold>

//<editor-fold desc="network">
internal class TelegramClient(
    client: HttpClient? = null,
    private val api: Api = DefaultApi,
    private val defaultTimeout: Long
) {
    private val client = client ?: HttpClient(Apache) { engine { socketTimeout = 0 /* workaround for custom timeouts in ktor 1.2.0 */ }}

    private val logger = KotlinLogging.logger { }

    private val json = Json(
        JsonConfiguration.Stable.copy(
            strictMode = false, // for parsing Message
            encodeDefaults = false
        )
    )

    /** Close client */
    fun close() = client.close()

    /**
     * Make request [request] to telegram network, for bot with token [token]
     *
     * @param timeout timeout (in millis)
     * @return on success telegram answer parsed to [T] is returned
     */
    suspend fun <T : Any> makeRequest(token: String, request: Request<T>, timeout: Long? = null): T = try {
        logger.debug { "Sending request to telegram api: $request" }

        val response = callTelegram(token, request, timeout ?: defaultTimeout)
        val result = parseTelegramAnswer(response, request.resultDeserializer)

        logger.debug { "Request [$request] was successfully made to telegram api with result=$response" }

        result
    } catch (e: ResponseException) { throw checkTelegramError(e) }

    suspend fun downloadFile(token: String, path: String, destination: Path) {
        logger.debug { "Downloading file from (telegram)/$path to $destination..." }

        val response = client.call {
            url(api.fileUrl(token, path))
        }.response

        withContext(Dispatchers.IO) {
            if (!Files.exists(destination)) {
                Files.createDirectories(destination.parent)
                Files.createFile(destination)
            }

            Files.newOutputStream(destination).use { it.write(response.readBytes()) }
        }

        logger.debug { "File downloaded from (telegram)/$path to $destination" }
    }

    //<editor-fold desc="private">
    private suspend fun <T : Any> callTelegram(token: String, request: Request<T>, timeout: Long): String = (withTimeoutOrNull(timeout) { // workaround for custom timeouts in ktor 1.2.0
        when (request) {
            is MultipartRequest<T> -> prepareCall(token, request)
            is SimpleRequest<T> -> prepareCall(token, request)
        }
    } ?: throw TimeoutException(timeout)).response.use { it.readText() }

    /**
     * Checks answer from telegram on errors.
     *
     * @return Detected exception
     */
    private suspend fun checkTelegramError(exception: ResponseException): TelegramAPIException {
        val response = exception.response.readText()
        val answer = json.parse(TResponse.serializer(Unit.serializer()), response)
        return checkTelegramError(answer)
    }

    /**
     * Parses answer from telegram, to common kotlin type.
     *
     * @param response response from telegram
     */
    private fun <T> parseTelegramAnswer(response: String, deserializer: KSerializer<T>): T {
        // Parse telegram-answer
        val answer: TResponse<T> = json.parse(TResponse.serializer(deserializer), response)

        // If there is result, telegram says that 'everything ok' and http status code means ~'no error', than return result
        if (
            answer.result != null &&
            answer.ok &&
            answer.errorCode?.let { HttpStatusCode.OK <= it } != false &&
            answer.errorCode?.let { it <= HttpStatusCode(226, "IM_USED") } != false
        ) {
            return answer.result
        }
        // else - throw error
        throw checkTelegramError(answer)
    }

    /**
     * Checks answer from telegram on errors.
     *
     * @param answer answer from telegram
     * @return Detected exception
     */
    private fun checkTelegramError(answer: TResponse<*>): TelegramAPIException {
        // Check if telegram-answer has parameters (in witch case return corresponding exceptions)
        when {
            answer.parameters?.migrateCoChatId != null -> return MigrateToChat(answer.parameters.migrateCoChatId)
            answer.parameters?.retryAfter != null -> return RetryAfter(answer.parameters.retryAfter)
        }

        // Check if http status code is corresponding to some error (and return it if is)
        if (answer.errorDescription != null) when (answer.errorCode) {
            HttpStatusCode.BadRequest.value -> return BadRequestExceptionDetector.detect(answer.errorDescription)
            HttpStatusCode.NotFound.value -> return NotFoundExceptionDetector.detect(answer.errorDescription)
            HttpStatusCode.Conflict.value -> return ConflictExceptionDetector.detect(answer.errorDescription)
            HttpStatusCode.Unauthorized.value, HttpStatusCode.Forbidden.value -> return UnauthorizedDetector.detect(answer.errorDescription)
        }

        if (answer.errorCode == HttpStatusCode.RequestEntityTooLarge.value)
            return NetworkError("File too large for uploading. Check telegram network limits https://core.telegram.org/bots/network#senddocument")

        // I don't understand it, you can ask developer of `aiogram` about this :P
        if (answer.errorCode != null && answer.errorCode >= HttpStatusCode.InternalServerError) {
            if ("restart" in answer.errorDescription!!) {
                return RestartingTelegram()
            }
        }

        // If errors wasn't found, return common TelegramAPIException
        return TelegramAPIException("${answer.errorDescription} [${answer.errorCode}]")
    }


    private suspend fun <R : SimpleRequest<*>> prepareCall(token: String, request: R): HttpClientCall =
        client.tpost(api.url(token, request.method)) {
            body = TextContent(
                request.stringify(json),
                ContentType.Application.Json
            )
        }

    private suspend fun <R : MultipartRequest<*>> prepareCall(token: String, request: R): HttpClientCall =
        client.tpost(api.url(token, request.method)) {
            accept(ContentType.Application.Json)

            body = MultiPartFormDataContent(
                formData {
                    // I don't understand how it works :\
                    // TODO: rewrite multipart requests
                    request.paramsJson(json).jsonObject.forEach { (key, value) ->
                        if ((request.attach || key !in request.mediaMap.keys) && !value.isNull) {
                            if (value is JsonPrimitive) append(key, value.content)
                            else append(key, value.toString())
                        }
                    }

                    request.mediaMap.forEach { (key, value) ->
                        val headers = Headers.build {
                            append(HttpHeaders.ContentType, value.mimeType)
                            append(HttpHeaders.ContentDisposition, "filename=${value.filename}")
                        }
                        appendInput(key, headers) { value.file.inputStream().asInput() }
                    }
                }
            )
        }

    private suspend fun HttpClient.tpost(url: String, block: HttpRequestBuilder.() -> Unit): HttpClientCall = call(url) {
        method = HttpMethod.Post
        block()
    }
    //</editor-fold>

}
//</editor-fold>

//<editor-fold desc="http code helpers">
private val HttpStatusCode.Companion.RequestEntityTooLarge by lazy {
    HttpStatusCode(413, "REQUEST_ENTITY_TOO_LARGE")
}

/** Needed for [checkTelegramError] */
private operator fun Int.compareTo(other: HttpStatusCode): Int = this - other.value

/** Needed for [checkTelegramError] */
private operator fun HttpStatusCode.compareTo(other: Int): Int = this.value - other
//</editor-fold>