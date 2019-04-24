package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a video file. */
@Serializable data class Video(
    @SerialName("file_id") val fileId: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val thumb: PhotoSize? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Int? = null
)