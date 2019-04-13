package telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents an audio file to be treated as music by the Telegram clients. */
@Serializable data class Audio(
    @SerialName("file_id") val fileId: String,
    val duration: Int,
    @Optional val performer: String? = null,
    @Optional val title: String? = null,
    @Optional @SerialName("mime_type") val mimeType: String? = null,
    @Optional @SerialName("file_size") val fileSize: Int? = null,
    @Optional val thumb: PhotoSize? = null
)