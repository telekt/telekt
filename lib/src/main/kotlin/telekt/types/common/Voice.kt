package telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a voice note. */
@Serializable data class Voice(
    @SerialName("file_id") val fileId: String,
    val duration: Int,
    @Optional @SerialName("mime_type") val mimeType: String? = null,
    @Optional @SerialName("file_size") val fileSize: Int? = null
)