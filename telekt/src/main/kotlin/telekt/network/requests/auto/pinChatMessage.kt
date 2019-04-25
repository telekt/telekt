package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.util.Recipient
import rocks.waffle.telekt.util.serializer


/**
 * [PinChatMessage] request.
 * Use this method to pin a message in a supergroup or a channel. The bot must be an administrator in the chat for this to work and must have the ‘can_pin_messages’ admin right in the supergroup or ‘can_edit_messages’ admin right in the channel. Returns True on success.
 * More: https://core.telegram.org/bots/api#pinchatmessage
 */
@Serializable data class PinChatMessage(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Identifier of a message to pin */
    @SerialName("message_id") val messageId: Int,
    /** Pass True, if it is not necessary to send a notification to all chat members about the new pinned message. Notifications are always disabled in channels. */
    @SerialName("disable_notification") val disableNotification: Boolean? = null
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.pinChatMessage
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(PinChatMessage.serializer(), this)
}
