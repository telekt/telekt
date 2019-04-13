package telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import telekt.network.InputFile
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.SimpleRequest
import telekt.util.Recipient
import telekt.util.serializer


/**
 * [SetChatPhoto] request.
 * Use this method to set a new profile photo for the chat. Photos can't be changed for private chats. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Returns True on success. Note: In regular groups (non-supergroups), this method will only work if the ‘All Members Are Admins’ setting is off in the target group.
 * More: https://core.telegram.org/bots/api#setchatphoto
 */
@Serializable data class SetChatPhoto(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** New chat photo, uploaded using multipart/form-data */
    val photo: InputFile
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.setChatPhoto
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(SetChatPhoto.serializer(), this)
}
