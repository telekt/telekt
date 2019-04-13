package telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import telekt.contrib.filters.TextableTelegramEvent

/** This object represents an incoming callback query from a callback button in an inline keyboard. If the button that originated the query was attached to a message sent by the bot, the field message will be present. If the button was attached to a message sent via the bot (in inline mode), the field inline_message_id will be present. Exactly one of the fields data or game_short_name will be present. */
@Serializable data class CallbackQuery(
    val id: String,
    val from: User,
    @Optional val message: Message? = null,
    @Optional @SerialName("inline_message_id") val inlineMessageId: String? = null,
    @SerialName("chat_instance") val chatInstance: String,
    @Optional val data: String? = null,
    @Optional @SerialName("game_short_name") val gameShortName: String? = null
) : TextableTelegramEvent {
    @Transient    override val eventText: String?
        get() = data
}