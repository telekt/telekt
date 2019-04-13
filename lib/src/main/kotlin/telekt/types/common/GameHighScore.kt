package telekt.types

import kotlinx.serialization.Serializable

/** This object represents one row of the high scores table for a game. */
@Serializable data class GameHighScore(
    val position: Int,
    val user: User,
    val score: Int
)