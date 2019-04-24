package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents an audio file to be treated as music by the Telegram clients. */
@Serializable data class Audio(
    @SerialName("file_id") val fileId: String,
    val duration: Int,
    val performer: String? = null,
    val title: String? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Int? = null,
    val thumb: PhotoSize? = null
)