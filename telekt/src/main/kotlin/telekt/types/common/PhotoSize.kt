package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents one size of a photo or a file / sticker thumbnail. */
@Serializable data class PhotoSize(
    @SerialName("file_id") val fileId: String,
    val width: Int,
    val height: Int,
    @Optional @SerialName("file_size") val fileSize: Int? = null
)