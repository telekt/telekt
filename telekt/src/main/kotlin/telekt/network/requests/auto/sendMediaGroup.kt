package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import rocks.waffle.telekt.network.MultipartFile
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.MultipartRequest
import rocks.waffle.telekt.types.InputMedia
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.util.Recipient


/**
 * [SendMediaGroup] request.
 * Use this method to send a group of photos or videos as an album. On success, an array of the sent Messages is returned.
 * More: https://core.telegram.org/bots/api#sendmediagroup
 */
@Serializable data class SendMediaGroup(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** A JSON-serialized array describing photos and videos to be sent, must include 2–10 items */
    val media: List<InputMedia>,
    /** Sends the messages silently. Users will receive a notification with no sound. */
    @Optional @SerialName("disable_notification") val disableNotification: Boolean? = null,
    /** If the messages are a reply, ID of the original message */
    @Optional @SerialName("reply_to_message_id") val replyToMessageId: Int? = null
) : MultipartRequest<List<Message>>() {
    @Transient override val method = TelegramMethod.sendMediaGroup
    @Transient override val resultDeserializer: KSerializer<out List<Message>> = Message.serializer().list

    @Transient override val mediaMap: Map<String, MultipartFile> = media.map {
        it.files.mapNotNull { file ->
            when (file) {
                is MultipartFile -> file.fileId to file
                else -> null
            }
        }
    }.inflate()

    override fun paramsJson(json: Json): JsonElement = json.toJson(SendMediaGroup.serializer(), this)

    override val attach: Boolean = true
}

fun <K, V> Collection<Collection<Pair<K, V>>>.inflate(): Map<K, V> =
    if (isEmpty()) mapOf()
    else fold(mapOf()) { acc, n -> acc + n }
