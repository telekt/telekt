package telekt.types

import kotlinx.serialization.Serializable

/** This object represents a point on the map. */
@Serializable data class Location(
    val longitude: Float,
    val latitude: Float
)