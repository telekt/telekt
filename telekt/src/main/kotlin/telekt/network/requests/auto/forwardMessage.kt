package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.util.Recipient


/**
 * [ForwardMessage] request.
 * Use this method to forward messages of any kind. On success, the sent Message is returned.
 * More: https://core.telegram.org/bots/api#forwardmessage
 */
@Serializable data class ForwardMessage(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Unique identifier for the chat where the original message was sent (or channel username in the format @channelusername) */
    @SerialName("from_chat_id") val fromChatId: Recipient,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @SerialName("disable_notification") val disableNotification: Boolean? = null,
    /** Message identifier in the chat specified in from_chat_id */
    @SerialName("message_id") val messageId: Int
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.forwardMessage
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(ForwardMessage.serializer(), this)
}
