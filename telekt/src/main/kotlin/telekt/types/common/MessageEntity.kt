package rocks.waffle.telekt.types

import kotlinx.serialization.Serializable
import rocks.waffle.telekt.types.enums.MessageEntityType

/** This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc. */
@Serializable data class MessageEntity(
    val type: MessageEntityType,
    val offset: Int,
    val length: Int,
    val url: String? = null,
    val user: User? = null
)