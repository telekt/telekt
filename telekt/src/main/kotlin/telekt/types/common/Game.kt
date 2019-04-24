package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a game. Use BotFather to create and edit games, their short names will act as unique identifiers. */
@Serializable data class Game(
    val title: String,
    val description: String,
    val photo: Array<PhotoSize>,
    val text: String? = null,
    @SerialName("text_entities") val textEntities: Array<MessageEntity>? = null,
    val animation: Animation? = null
)