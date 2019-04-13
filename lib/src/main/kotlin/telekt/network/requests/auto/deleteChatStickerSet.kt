package telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.SimpleRequest
import telekt.util.Recipient
import telekt.util.serializer


/**
 * [DeleteChatStickerSet] request.
 * Use this method to delete a group sticker set from a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
 * More: https://core.telegram.org/bots/api#deletechatstickerset
 */
@Serializable data class DeleteChatStickerSet(
    /** Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername) */
    @SerialName("chat_id") val chatId: Recipient
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.deleteChatStickerSet
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(DeleteChatStickerSet.serializer(), this)
}
