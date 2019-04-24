package rocks.waffle.telekt.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import rocks.waffle.telekt.network.requests.abstracts.Request
import java.nio.file.Path
import java.nio.file.Paths

interface Api {
    suspend fun <T : Any> makeRequest(token: String, request: Request<T>): T
    suspend fun downloadFile(token: String, path: String, destination: Path): Unit
    suspend fun close(): Unit
}

suspend fun Api.downloadFile(token: String, path: String, destination: String): Unit = downloadFile(token, path, Paths.get(destination))

/**
 * Represents telegram answer для каждого запроса
 *
 * More: https://core.telegram.org/bots/api#making-requests
 */
@Serializable
data class TResponse<T>( // Needed for parsing telegram response with Jackson.
    val ok: Boolean,
    val result: T? = null,
    @SerialName("description") val errorDescription: String? = null,
    @SerialName("error_code") val errorCode: Int? = null,
    val parameters: ResponseParameters? = null
)

@Serializable
data class ResponseParameters(
    @SerialName("migrate_to_chat_id") val migrateCoChatId: Long? = null,
    @SerialName("retry_after") val retryAfter: Int? = null
)