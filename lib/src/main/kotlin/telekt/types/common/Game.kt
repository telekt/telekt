package telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a game. Use BotFather to create and edit games, their short names will act as unique identifiers. */
@Serializable data class Game(
    val title: String,
    val description: String,
    val photo: Array<PhotoSize>,
    @Optional val text: String? = null,
    @Optional @SerialName("text_entities") val textEntities: Array<MessageEntity>? = null,
    @Optional val animation: Animation? = null
)