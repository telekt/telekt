package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import rocks.waffle.telekt.contrib.filters.TextableTelegramEvent
import rocks.waffle.telekt.types.events.TelegramEvent

/** This object contains information about one answer option in a poll. */
@Serializable data class PollOption(
    val text: String,
    @SerialName("voter_count") val voterCount: Int
)

/** This object contains information about a poll. */
@Serializable data class Poll(
    val id:	String,
    val question: String,
    val options: List<PollOption>,
    @SerialName("is_closed") val isClosed: Boolean
) : TextableTelegramEvent {
    @Transient override val eventText: String? get() = question
}