package telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.SimpleRequest
import telekt.types.Message
import telekt.types.ReplyMarkup
import telekt.types.enums.ParseMode
import telekt.util.Recipient


/**
 * [SendMessage] request.
 * Use this method to send text messages. On success, the sent Message is returned.
 * More: https://core.telegram.org/bots/api#sendmessage
 */
@Serializable data class SendMessage(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Text of the message to be sent */
    val text: String,
    /** Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in your bot's message. */
    @Optional @SerialName("parse_mode") val parseMode: ParseMode? = null,
    /** Disables link previews for links in this message */
    @Optional @SerialName("disable_web_page_preview") val disableWebPagePreview: Boolean? = null,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @Optional @SerialName("disable_notification") val disableNotification: Boolean? = null,
    /** If the message is a reply, ID of the original message */
    @Optional @SerialName("reply_to_message_id") val replyToMessageId: Int? = null,
    /** Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user. */
    @Optional @SerialName("reply_markup") val replyMarkup: ReplyMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.sendMessage
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(SendMessage.serializer(), this)
}
