package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.ChatMember
import rocks.waffle.telekt.util.Recipient


/**
 * [GetChatAdministrators] request.
 * Use this method to get a list of administrators in a chat. On success, returns an Array of ChatMember objects that contains information about all chat administrators except other bots. If the chat is a group or a supergroup and no administrators were appointed, only the creator will be returned.
 * More: https://core.telegram.org/bots/api#getchatadministrators
 */
@Serializable data class GetChatAdministrators(
    /** Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient
) : SimpleRequest<List<ChatMember>>() {
    @Transient override val method = TelegramMethod.getChatAdministrators
    @Transient override val resultDeserializer: KSerializer<out List<ChatMember>> = ChatMember.serializer().list
    override fun stringify(json: Json): String = json.stringify(GetChatAdministrators.serializer(), this)
}
