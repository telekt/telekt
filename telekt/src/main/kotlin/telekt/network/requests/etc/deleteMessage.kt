package rocks.waffle.telekt.network.requests.etc

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.util.Recipient
import rocks.waffle.telekt.util.serializer

/**
 * [DeleteMessage] request.
 *
 * Use this method to delete a message, including service messages, with the following limitations:
 *   - A message can only be deleted if it was sent less than 48 hours ago.
 *   - Bots can delete outgoing messages in private chats, groups, and supergroups.
 *   - Bots can delete incoming messages in private chats.
 *   - Bots granted can_post_messages permissions can delete outgoing messages in channels.
 *   - If the bot is an administrator of a group, it can delete any message there.
 *   - If the bot has can_delete_messages permission in a supergroup or a channel, it can delete any message there.
 *
 * More: https://core.telegram.org/bots/api#deletemessage
 */
@Serializable data class DeleteMessage(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Identifier of the message to delete */
    @SerialName("message_id") val messageId: Int
) : SimpleRequest<Unit>() {
    @Transient override val method: TelegramMethod = TelegramMethod.deleteMessage
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(DeleteMessage.serializer(), this)
}