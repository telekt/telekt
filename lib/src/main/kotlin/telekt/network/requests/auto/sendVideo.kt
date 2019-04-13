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
 * [SendVideo] request.
 * Use this method to send video files, Telegram clients support mp4 videos (other formats may be sent as Document). On success, the sent Message is returned. Bots can currently send video files of up to 50 MB in size, this limit may be changed in the future.
 * More: https://core.telegram.org/bots/api#sendvideo
 */
@Serializable data class SendVideo(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Video to send. Pass a file_id as String to send a video that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get a video from the Internet, or upload a new video using multipart/form-data. More info on Sending Files » */
    val video: InputFile,
    /** Duration of sent video in seconds */
    @Optional val duration: Int? = null,
    /** Video width */
    @Optional val width: Int? = null,
    /** Video height */
    @Optional val height: Int? = null,
    /** Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail‘s width and height should not exceed 90. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can’t be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files » */
    @Optional val thumb: InputFile? = null,
    /** Video caption (may also be used when resending videos by file_id), 0-1024 characters */
    @Optional val caption: String? = null,
    /** Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption. */
    @SerialName("parse_mode") @Optional val parseMode: ParseMode? = null,
    /** Pass True, if the uploaded video is suitable for streaming */
    @SerialName("supports_streaming") @Optional val supportsStreaming: Boolean? = null,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @SerialName("disable_notification") @Optional val disableNotification: Boolean? = null,
    /** If the message is a reply, ID of the original message */
    @SerialName("reply_to_message_id") @Optional val replyToMessageId: Int? = null,
    /** Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user. */
    @SerialName("reply_markup") @Optional val replyMarkup: ReplyMarkup? = null
) : MultipartRequest<Message>() {
    @Transient override val method = TelegramMethod.sendVideo
    @Transient override val mediaMap: Map<String, MultipartFile> = mapOf("video" to video, "thumb" to thumb).asMediaMap()

    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun paramsJson(json: Json): JsonElement = json.toJson(SendVideo.serializer(), this)
}
