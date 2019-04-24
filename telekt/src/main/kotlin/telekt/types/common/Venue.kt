package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a venue. */
@Serializable data class Venue(
    val location: Location,
    val title: String,
    val address: String,
    @SerialName("foursquare_id") val foursquareId: String? = null,
    @SerialName("foursquare_type") val foursquareType: String? = null
)