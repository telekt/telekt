package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import rocks.waffle.telekt.contrib.filters.TextableTelegramEvent

/** This object represents an incoming inline query. When the user sends an empty query, your bot could return some default or trending results. */
@Serializable data class InlineQuery(
    val id: String,
    val from: User,
    @Optional val location: Location? = null,
    val query: String,
    val offset: String
) : TextableTelegramEvent {
    @Transient    override val eventText: String?
        get() = query
}