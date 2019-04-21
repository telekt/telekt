package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.util.Recipient
import rocks.waffle.telekt.util.serializer


/**
 * [SetChatStickerSet] request.
 * Use this method to set a new group sticker set for a supergroup. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
 * More: https://core.telegram.org/bots/api#setchatstickerset
 */
@Serializable data class SetChatStickerSet(
    /** Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Name of the sticker set to be set as the group sticker set */
    @SerialName("sticker_set_name") val stickerSetName: String
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.setChatStickerSet
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(SetChatStickerSet.serializer(), this)
}
