package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents an animation file (GIF or H.264/MPEG-4 AVC video without sound). */
@Serializable data class Animation(
    @SerialName("file_id") val fileId: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val thumb: PhotoSize? = null,
    @SerialName("file_name") val fileName: String? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Int? = null
)