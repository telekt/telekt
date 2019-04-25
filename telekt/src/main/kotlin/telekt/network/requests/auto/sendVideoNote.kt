package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import rocks.waffle.telekt.network.InputFile
import rocks.waffle.telekt.network.MultipartFile
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.MultipartRequest
import rocks.waffle.telekt.network.requests.abstracts.asMediaMap
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.ReplyMarkup
import rocks.waffle.telekt.util.Recipient


/**
 * [SendVideoNote] request.
 * As of v.4.0, Telegram clients support rounded square mp4 videos of up to 1 minute long. Use this method to send video messages. On success, the sent Message is returned.
 * More: https://core.telegram.org/bots/api#sendvideonote
 */
@Serializable data class SendVideoNote(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Video note to send. Pass a file_id as String to send a video note that exists on the Telegram servers (recommended) or upload a new video using multipart/form-data. More info on Sending Files ». Sending video notes by a URL is currently unsupported */
    val videoNote: InputFile,
    /** Duration of sent video in seconds */
    val duration: Int? = null,
    /** Video width and height, i.e. diameter of the video message */
    val length: Int? = null,
    /** Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail‘s width and height should not exceed 90. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can’t be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files » */
    val thumb: InputFile? = null,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @SerialName("disable_notification") val disableNotification: Boolean? = null,
    /** If the message is a reply, ID of the original message */
    @SerialName("reply_to_message_id") val replyToMessageId: Int? = null,
    /** Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user. */
    @SerialName("reply_markup") val replyMarkup: ReplyMarkup? = null
) : MultipartRequest<Message>() {
    @Transient override val method = TelegramMethod.sendVideoNote
    @Transient override val mediaMap: Map<String, MultipartFile> = mapOf("video_note" to videoNote, "thumb" to thumb).asMediaMap()

    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun paramsJson(json: Json): JsonElement = json.toJson(SendVideoNote.serializer(), this)
}
