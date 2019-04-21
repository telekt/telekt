package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a video message (available in Telegram apps as of v.4.0). */
@Serializable data class VideoNote(
    @SerialName("file_id") val fileId: String,
    val length: Int,
    val duration: Int,
    @Optional val thumb: PhotoSize? = null,
    @Optional @SerialName("file_size") val fileSize: Int? = null
)