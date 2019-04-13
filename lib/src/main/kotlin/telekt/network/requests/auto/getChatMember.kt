package telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.SimpleRequest
import telekt.types.ChatMember
import telekt.util.Recipient


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
