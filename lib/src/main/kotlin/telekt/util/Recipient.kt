package rocks.waffle.telekt.util

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor


/**
 * Creates new instance of [Recipient] from [channelUsername].
 * NOTE: [channelUsername] should NOT start from '@', '@' appended automatically
 * NOTE: this works ONLY for channel usernames, not for user usernames
 */
fun Recipient(channelUsername: String): Recipient = Recipient.ChannelUsername.new(channelUsername)

/** Creates new instance of [Recipient] from [chatId] */
fun Recipient(chatId: Long): Recipient = Recipient.ChatId.new(chatId)


/**
 * In telegram api param `chat_id` commonly can be `Long or String` -- chat id or _channel_ username.
 * This sealed class covers both variants of recipient.
 */
@Serializable(with = RecipientSerializer::class) sealed class Recipient {
    /** Channel username recipient. */
    class ChannelUsername private constructor(val username: String) : Recipient() {
        companion object {
            /**
             * Creates new instance of [ChannelUsername] from [username]
             * NOTE: [username] should NOT start from '@', '@' appended automatically
             */
            fun new(username: String): ChannelUsername = ChannelUsername("@$username")

            /**
             * Creates new instance of [ChannelUsername] from [username]
             * Differents between [newFromRawUsername] and [new] that [newFromRawUsername] doesn't add '@' to the start of [username]
             */
            fun newFromRawUsername(username: String): ChannelUsername = ChannelUsername(username)
        }
    }

    /** Chat id recipient */
    class ChatId private constructor(val id: Long) : Recipient() {
        companion object {
            /** Creates new instance of [ChatId]  from [id]*/
            fun new(id: Long): ChatId = ChatId(id)
        }
    }

    override fun toString(): String = when (this) {
        is ChannelUsername -> "rocks.waffle.telekt.util.Recipient($username)"
        is ChatId -> "rocks.waffle.telekt.util.Recipient($id)"
    }
}

/**
 * Kotlinx/serialization serializer for [Recipient].
 * Decodes: Long as [Recipient.ChatId], String as [Recipient.ChannelUsername]
 * Encodes: [Recipient.ChatId] as Long, [Recipient.ChannelUsername] as String
 */
object RecipientSerializer : KSerializer<Recipient> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("RecipientSerializer")

    override fun deserialize(decoder: Decoder): Recipient {
        val usernameOrId = decoder.decodeString()
        val id = usernameOrId.toLongOrNull()
        return if (id != null) Recipient(id) else Recipient(usernameOrId)
    }

    override fun serialize(encoder: Encoder, obj: Recipient) = when (obj) {
        is Recipient.ChannelUsername -> encoder.encodeString(obj.username)
        is Recipient.ChatId -> encoder.encodeLong(obj.id)
    }
}