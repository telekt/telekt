package telekt.network.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.receive
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.BadResponseStatusException
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readBytes
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.io.readUTF8Line
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import telekt.exceptions.*
import telekt.network.Api
import telekt.network.TResponse
import telekt.network.ktor.callfactory.KtorCallFactory
import telekt.network.ktor.callfactory.MultipartRequestCallFactory
import telekt.network.ktor.callfactory.SimpleCallFactory
import telekt.network.requests.abstracts.MultipartRequest
import telekt.network.requests.abstracts.Request
import telekt.network.requests.abstracts.SimpleRequest
import java.nio.file.Files
import java.nio.file.Path

const val API_HOST = "api.telegram.org"

const val API_PATH_PATTERN = "/bot%s"
const val FILE_PATH_PATTERN = "/file/bot%s/%s"


data class CallFactories(
    val simpleCallFactory: KtorCallFactory<Any, SimpleRequest<Any>> = SimpleCallFactory(),
    val multipartCallFactory: KtorCallFactory<Any, MultipartRequest<Any>> = MultipartRequestCallFactory()
)

val HttpStatusCode.Companion.RequestEntityTooLarge by lazy {
    HttpStatusCode(413, "REQUEST_ENTITY_TOO_LARGE")
}

class KtorApi(
    host: String = API_HOST,
    pollingClient: HttpClient? = null,
    client: HttpClient? = null,
    private val factories: CallFactories = CallFactories()
) : Api {
    private val pollingClient = pollingClient
        ?: HttpClient(Apache) { engine { socketTimeout = 25_000 /* Needed for polling */ } }
    private val client = client ?: HttpClient(Apache)

    private val logger = KotlinLogging.logger { }

    private val baseUrl = "https://$host$API_PATH_PATTERN"

    private val jsonParser = Json(strictMode = false)


    /** Close client */
    override suspend fun close() {
        pollingClient.close()
        client.close()
    }

    /** Needed for [checkTelegramError] */
    private operator fun Int.compareTo(other: HttpStatusCode): Int = this - other.value

    /** Needed for [checkTelegramError] */
    private operator fun HttpStatusCode.compareTo(other: Int): Int = this.value - other

    /** Needed for [checkTelegramError] */
    private infix fun Int.equals(other: HttpStatusCode): Boolean = this == other.value


    /**
     * Parses answer from telegram, to common kotlin type.
     *
     * @param response response from telegram
     * @param stringResponse response from telegram converted to string
     */
    private fun <T> parseTelegramAnswer(response: HttpResponse, stringResponse: String, deserializer: KSerializer<T>): T {
        // Check is content-type right (it must be "application/json")
        val contentType = response.headers["Content-Type"]
        if (contentType != "application/json") {
            throw NetworkError("Invalid response with content type $contentType: \"$stringResponse\"")
        }

        // Parse telegram-answer
        val answer: TResponse<T> = jsonParser.parse(TResponse.serializer(deserializer), stringResponse)

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
     * @param status http status code returned by telegram
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
            return NetworkError("File too large for uploading. Check telegram api limits https://core.telegram.org/bots/api#senddocument")

        // I don't understand it, you can ask developer of `aiogram` about this :P
        if (answer.errorCode != null && answer.errorCode >= HttpStatusCode.InternalServerError) {
            if ("restart" in answer.errorDescription!!) {
                return RestartingTelegram()
            }
        }

        // If errors wasn't found, return common TelegramAPIException
        return TelegramAPIException("${answer.errorDescription} [${answer.errorCode}]")
    }


    /**
     * Make request [request] to telegram api, for bot with token [token]
     *
     * @return on success telegram answer parsed to [T] is returned
     */
    override suspend fun <T : Any> makeRequest(token: String, request: Request<T>): T {
        logger.debug { "Sending request to telegram api: $request" }
        try {
            val url = baseUrl.format(token)
            val call = when (request) {
                is MultipartRequest<T> -> factories.multipartCallFactory.prepareCall(client, url, request)
                is SimpleRequest<T> -> factories.simpleCallFactory.prepareCall(client, url, request)
            }

            val stringResponse = call.response.receive<String>()

            val result = parseTelegramAnswer(call.response, stringResponse, request.resultDeserializer)
            logger.debug { "Request [$request] was successfully made to telegram api with result=$stringResponse" }
            return result
        } catch (e: BadResponseStatusException) {
            val response = e.response.content.readUTF8Line() ?: throw UnknownTelegramException(e.response.content.toString())
            val answer = Json.parse(TResponse.serializer(request.resultDeserializer), response)
            throw checkTelegramError(answer)
        }
    }

    override suspend fun downloadFile(token: String, path: String, destination: Path) {
        logger.debug { "Downloading file from (telegram)/$path to $destination..." }
        val response = client.call {
            url {
                protocol = URLProtocol.HTTPS
                host = API_HOST
                encodedPath = FILE_PATH_PATTERN.format(token, path)
            }
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
}
