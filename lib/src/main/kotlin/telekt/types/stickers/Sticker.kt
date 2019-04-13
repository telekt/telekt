package telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a sticker. */
@Serializable data class Sticker(
    @SerialName("file_id") val fileId: String,
    val width: Int,
    val height: Int,
    @Optional val thumb: PhotoSize? = null,
    @Optional val emoji: String? = null,
    @Optional @SerialName("set_name") val setName: String? = null,
    @Optional @SerialName("mask_position") val maskPosition: MaskPosition? = null,
    @Optional @SerialName("file_size") val fileSize: Int? = null
)