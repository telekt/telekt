package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import rocks.waffle.telekt.contrib.filters.TextableTelegramEvent

/** Represents a result of an inline query that was chosen by the user and sent to their chat partner. */
@Serializable data class ChosenInlineResult(
    @SerialName("result_id") val resultId: String,
    val from: User,
    @Optional val location: Location? = null,
    @Optional @SerialName("inline_message_id") val inlineMessageId: String? = null,
    val query: String
) : TextableTelegramEvent {
    @Transient    override val eventText: String?
        get() = query
}
