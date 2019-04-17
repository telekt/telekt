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
 * [LeaveChat] request.
 * Use this method for your bot to leave a group, supergroup or channel. Returns True on success.
 * More: https://core.telegram.org/bots/api#leavechat
 */
@Serializable data class LeaveChat(
    /** Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.leaveChat
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(LeaveChat.serializer(), this)
}
