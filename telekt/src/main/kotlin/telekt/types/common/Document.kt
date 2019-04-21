package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a general file (as opposed to photos, voice messages and audio files). */
@Serializable data class Document(
    @SerialName("file_id") val fileId: String,
    @Optional val thumb: PhotoSize? = null,
    @Optional @SerialName("file_name") val fileName: String? = null,
    @Optional @SerialName("mime_type") val mimeType: String? = null,
    @Optional @SerialName("file_size") val fileSize: Int? = null
)