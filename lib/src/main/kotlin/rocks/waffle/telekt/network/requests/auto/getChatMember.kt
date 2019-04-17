package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.ChatMember
import rocks.waffle.telekt.util.Recipient


/**
 * [GetChatMember] request.
 * Use this method to get information about a member of a chat. Returns a ChatMember object on success.
 * More: https://core.telegram.org/bots/api#getchatmember
 */
@Serializable data class GetChatMember(
    /** Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Unique identifier of the target user */
    @SerialName("user_id") val userId: Int
) : SimpleRequest<ChatMember>() {
    @Transient override val method = TelegramMethod.getChatMember
    @Transient override val resultDeserializer: KSerializer<out ChatMember> = ChatMember.serializer()
    override fun stringify(json: Json): String = json.stringify(GetChatMember.serializer(), this)
}
