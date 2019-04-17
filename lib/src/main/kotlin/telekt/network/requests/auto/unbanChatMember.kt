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
 * [UnbanChatMember] request.
 * Use this method to unban a previously kicked user in a supergroup or channel. The user will not return to the group or channel automatically, but will be able to join via link, etc. The bot must be an administrator for this to work. Returns True on success.
 * More: https://core.telegram.org/bots/api#unbanchatmember
 */
@Serializable data class UnbanChatMember(
    /** Unique identifier for the target group or username of the target supergroup or channel (in the format @username) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Unique identifier of the target user */
    @SerialName("user_id") val userId: Int
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.unbanChatMember
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(UnbanChatMember.serializer(), this)
}
