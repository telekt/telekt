package telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import telekt.network.InputFile
import telekt.network.MultipartFile
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.MultipartRequest
import telekt.network.requests.abstracts.asMediaMap
import telekt.types.Message
import telekt.types.ReplyMarkup
import telekt.types.enums.ParseMode
import telekt.util.Recipient


/**
 * [SendAnimation] request.
 * Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound). On success, the sent Message is returned. Bots can currently send animation files of up to 50 MB in size, this limit may be changed in the future.
 * More: https://core.telegram.org/bots/api#sendanimation
 */
@Serializable data class SendAnimation(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Animation to send. Pass a file_id as String to send an animation that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an animation from the Internet, or upload a new animation using multipart/form-data. More info on Sending Files » */
    val animation: InputFile,
    /** Duration of sent animation in seconds */
    @Optional val duration: Int? = null,
    /** Animation width */
    @Optional val width: Int? = null,
    /** Animation height */
    @Optional val height: Int? = null,
    /** Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail‘s width and height should not exceed 90. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can’t be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files » */
    @Optional val thumb: InputFile? = null,
    /** Animation caption (may also be used when resending animation by file_id), 0-1024 characters */
    @Optional val caption: String? = null,
    /** Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption. */
    @SerialName("parse_mode") @Optional val parseMode: ParseMode? = null,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @SerialName("disable_notification") @Optional val disableNotification: Boolean? = null,
    /** If the message is a reply, ID of the original message */
    @SerialName("reply_to_message_id") @Optional val replyToMessageId: Int? = null,
    /** Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user. */
    @SerialName("reply_markup") @Optional val replyMarkup: ReplyMarkup? = null
) : MultipartRequest<Message>() {
    @Transient override val method = TelegramMethod.sendAnimation
    @Transient override val mediaMap: Map<String, MultipartFile> = mapOf("animation" to animation, "thumb" to thumb).asMediaMap()

    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()

    override fun paramsJson(json: Json): JsonElement = json.toJson(SendAnimation.serializer(), this)
}
