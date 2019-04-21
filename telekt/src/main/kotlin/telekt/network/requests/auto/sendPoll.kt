package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.ReplyMarkup
import rocks.waffle.telekt.util.Recipient
import kotlinx.serialization.Transient


/**
 * [SendPoll] request.
 * Use this method to send a native poll. A native poll can't be sent to a private chat.
 * On success, the sent Message is returned.
 *
 * More: https://core.telegram.org/bots/api#sendpoll
 */
@Serializable data class SendPoll(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername). A native poll can't be sent to a private chat. */
    @SerialName("chat_id") val chatId: Recipient,
    /** Poll question, 1-255 characters */
    val question: String,
    /** List of answer options, 2-10 strings 1-100 characters each */
    val options: List<String>,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @Optional @SerialName("disable_notification") val disableNotification: Boolean? = null,
    /** If the message is a reply, ID of the original message */
    @Optional @SerialName("reply_to_message_id") val replyToMessageId: Int? = null,
    /** Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user. */
    @Optional @SerialName("reply_markup") val replyMarkup: ReplyMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.sendPoll
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(SendPoll.serializer(), this)
}
