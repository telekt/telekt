package telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Contains information about why a request was unsuccessful. */
@Serializable data class ResponseParameters(
    @Optional @SerialName("migrate_to_chat_id") val migrateToChatId: Int? = null,
    @Optional @SerialName("retry_after") val retryAfter: Int? = null
)