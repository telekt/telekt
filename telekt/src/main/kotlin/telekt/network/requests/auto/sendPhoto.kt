package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import rocks.waffle.telekt.network.InputFile
import rocks.waffle.telekt.network.MultipartFile
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.MultipartRequest
import rocks.waffle.telekt.network.requests.abstracts.asMediaMap
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.enums.ParseMode
import rocks.waffle.telekt.types.replymarkup.ReplyMarkup
import rocks.waffle.telekt.util.Recipient


/**
 * [SendPhoto] request.
 * Use this method to send photos. On success, the sent Message is returned.
 * More: https://core.telegram.org/bots/api#sendphoto
 */
@Serializable data class SendPhoto(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a photo from the Internet, or upload a new photo using multipart/form-data. More info on Sending Files » */
    val photo: InputFile,
    /** Photo caption (may also be used when resending photos by file_id), 0-1024 characters */
    val caption: String? = null,
    /** Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption. */
    @SerialName("parse_mode") val parseMode: ParseMode? = null,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @SerialName("disable_notification") val disableNotification: Boolean? = null,
    /** If the message is a reply, ID of the original message */
    @SerialName("reply_to_message_id") val replyToMessageId: Int? = null,
    /** Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user. */
    @SerialName("reply_markup") val replyMarkup: ReplyMarkup? = null
) : MultipartRequest<Message>() {
    @Transient override val method = TelegramMethod.sendPhoto
    @Transient override val mediaMap: Map<String, MultipartFile> = mapOf("photo" to photo).asMediaMap()

    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun paramsJson(json: Json): JsonElement = json.toJson(SendPhoto.serializer(), this)
}
