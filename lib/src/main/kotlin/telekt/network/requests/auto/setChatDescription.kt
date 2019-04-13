package telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.SimpleRequest
import telekt.util.Recipient
import telekt.util.serializer


/**
 * [SetChatDescription] request.
 * Use this method to change the description of a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the appropriate admin rights. Returns True on success.
 * More: https://core.telegram.org/bots/api#setchatdescription
 */
@Serializable data class SetChatDescription(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** New chat description, 0-255 characters */
    @Optional val description: String? = null
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.setChatDescription
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(SetChatDescription.serializer(), this)
}
