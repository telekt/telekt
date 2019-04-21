package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represent a user's profile pictures. */
@Serializable data class UserProfilePhotos(
    @SerialName("total_count") val totalCount: Int,
    val photos: Array<Array<PhotoSize>>
)