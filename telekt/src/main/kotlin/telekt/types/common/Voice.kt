package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a voice note. */
@Serializable data class Voice(
    @SerialName("file_id") val fileId: String,
    val duration: Int,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Int? = null
)