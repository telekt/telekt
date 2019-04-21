package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.Chat
import rocks.waffle.telekt.util.Recipient


/**
 * [GetChat] request.
 * Use this method to get up to date information about the chat (current name of the user for one-on-one conversations, current username of a user, group or channel, etc.). Returns a Chat object on success.
 * More: https://core.telegram.org/bots/api#getchat
 */
@Serializable data class GetChat(
    /** Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient
) : SimpleRequest<Chat>() {
    @Transient override val method = TelegramMethod.getChat
    @Transient override val resultDeserializer: KSerializer<out Chat> = Chat.serializer()
    override fun stringify(json: Json): String = json.stringify(GetChat.serializer(), this)
}
