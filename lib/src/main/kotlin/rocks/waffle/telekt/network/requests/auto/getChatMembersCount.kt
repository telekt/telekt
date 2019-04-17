package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.util.Recipient


/**
 * [GetChatMembersCount] request.
 * Use this method to get the number of members in a chat. Returns Int on success.
 * More: https://core.telegram.org/bots/api#getchatmemberscount
 */
@Serializable data class GetChatMembersCount(
    /** Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient
) : SimpleRequest<Int>() {
    @Transient override val method = TelegramMethod.getChatMembersCount
    @Transient override val resultDeserializer: KSerializer<out Int> = Int.serializer()
    override fun stringify(json: Json): String = json.stringify(GetChatMembersCount.serializer(), this)
}
