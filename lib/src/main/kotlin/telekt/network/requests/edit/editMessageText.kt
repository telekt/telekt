package telekt.network.requests.edit

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.SimpleRequest
import telekt.types.InlineKeyboardMarkup
import telekt.types.Message
import telekt.types.enums.ParseMode
import telekt.util.Recipient
import telekt.util.serializer


/**
 * [EditMessageText] inline request.
 * Use this method to edit text and game messages sent via the bot (for inline bots).
 * More: https://core.telegram.org/bots/api#editmessagetext
 */
@Serializable data class EditMessageTextInline(
    /** Identifier of the inline message */
    @SerialName("inline_message_id") val inlineMessageId: String,
    /** New text of the message */
    val text: String,
    /** Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in your bot's message. */
    @Optional @SerialName("parse_mode") val parseMode: ParseMode? = null,
    /** Disables link previews for links in this message */
    @Optional @SerialName("disable_web_page_preview") val disableWebPagePreview: Boolean? = null,
    /** A JSON-serialized object for an inline keyboard. */
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.editMessageText
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(EditMessageTextInline.serializer(), this)
}


/**
 * [EditMessageText] request.
 * Use this method to edit text and game messages sent by the bot.
 * On success, the edited Message is returned.
 * More: https://core.telegram.org/bots/api#editmessagetext
 */
@Serializable data class EditMessageText(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Identifier of the sent message */
    @SerialName("message_id") val messageId: Int,
    /** New text of the message */
    val text: String,
    /** Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in your bot's message. */
    @Optional @SerialName("parse_mode") val parseMode: ParseMode? = null,
    /** Disables link previews for links in this message */
    @Optional @SerialName("disable_web_page_preview") val disableWebPagePreview: Boolean? = null,
    /** A JSON-serialized object for an inline keyboard. */
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.editMessageText
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(EditMessageText.serializer(), this)
}
