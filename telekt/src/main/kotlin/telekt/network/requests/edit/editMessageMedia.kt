package rocks.waffle.telekt.network.requests.edit

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.InlineKeyboardMarkup
import rocks.waffle.telekt.types.InputMedia
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.util.Recipient
import rocks.waffle.telekt.util.serializer


/**
 * [EditMessageMedia] inline request.
 * Use this method to edit animation, audio, document, photo, or video messages.
 * If a message is a part of a message album, then it can be edited only to a photo or a video.
 * Otherwise, message type can be changed arbitrarily.
 * New file can't be uploaded by this method (but can by [EditMessageMedia]). Use previously uploaded file via its file_id or specify a URL.
 * More: https://core.telegram.org/bots/api#editmessagemedia
 */
@Serializable data class EditMessageMediaInline(
    /** Identifier of the inline message */
    @SerialName("inline_message_id") val inlineMessageId: String? = null,
    /** A JSON-serialized object for a new media content of the message */
    val media: InputMedia,
    /** A JSON-serialized object for a new inline keyboard. */
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.editMessageMedia
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(EditMessageMediaInline.serializer(), this)
}


/**
 * [EditMessageMedia] request.
 * Use this method to edit animation, audio, document, photo, or video messages.
 * If a message is a part of a message album, then it can be edited only to a photo or a video.
 * Otherwise, message type can be changed arbitrarily.
 *  On success, the edited Message is returned.
 * More: https://core.telegram.org/bots/api#editmessagemedia
 */
@Serializable data class EditMessageMedia(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Identifier of the sent message */
    @SerialName("message_id") val messageId: Int,
    /** A JSON-serialized object for a new media content of the message */
    val media: InputMedia,
    /** A JSON-serialized object for a new inline keyboard. */
    @Optional @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.editMessageMedia
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(EditMessageMedia.serializer(), this)
}
