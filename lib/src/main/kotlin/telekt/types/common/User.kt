package telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a Telegram user or bot. */
@Serializable data class User(
    val id: Long,
    @SerialName("is_bot") val isBot: Boolean,
    @SerialName("first_name") val firstName: String,
    @Optional @SerialName("last_name") val lastName: String? = null,
    @Optional val username: String? = null,
    @Optional @SerialName("language_code") val languageCode: String? = null
)