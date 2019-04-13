package telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a chat photo. */
@Serializable data class ChatPhoto(
    @SerialName("small_file_id") val smallFileId: String,
    @SerialName("big_file_id") val bigFileId: String
)