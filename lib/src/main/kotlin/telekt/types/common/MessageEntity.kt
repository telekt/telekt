package telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import telekt.types.enums.MessageEntityType

/** This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc. */
@Serializable data class MessageEntity(
    val type: MessageEntityType,
    val offset: Int,
    val length: Int,
    @Optional val url: String? = null,
    @Optional val user: User? = null
)