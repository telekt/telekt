package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a general file (as opposed to photos, voice messages and audio files). */
@Serializable data class Document(
    @SerialName("file_id") val fileId: String,
    val thumb: PhotoSize? = null,
    @SerialName("file_name") val fileName: String? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Int? = null
)