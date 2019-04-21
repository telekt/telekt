package rocks.waffle.telekt.network.requests.edit

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.InlineKeyboardMarkup
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.enums.ParseMode
import rocks.waffle.telekt.util.Recipient
import rocks.waffle.telekt.util.serializer


/**
 * [EditMessageCaption] inline request.
 * Use this method to edit captions of messages sent via the bot (for inline bots).
 * More: https://core.telegram.org/bots/api#editmessagecaption
 */
@Serializable data class EditMessageCaptionInline(
    /** Identifier of the inline message */
    @SerialName("inline_message_id") val inlineMessageId: String,
    /** New caption of the message */
    @Optional val caption: String? = null,
    /** Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption. */
    @Optional @SerialName("parse_mode") val parseMode: ParseMode? = null,
    /** A JSON-serialized object for an inline keyboard. */
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.editMessageCaption
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(EditMessageCaptionInline.serializer(), this)
}


/**
 * [EditMessageCaption] request.
 * Use this method to edit captions of messages sent by the bot. On success, the edited Message is returned.
 * More: https://core.telegram.org/bots/api#editmessagecaption
 */
@Serializable data class EditMessageCaption(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Identifier of the sent message */
    @SerialName("message_id") val messageId: Int,
    /** New caption of the message */
    @Optional val caption: String? = null,
    /** Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption. */
    @Optional @SerialName("parse_mode") val parseMode: ParseMode? = null,
    /** A JSON-serialized object for an inline keyboard. */
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.editMessageCaption
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(EditMessageCaption.serializer(), this)
}
