package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a sticker. */
@Serializable data class Sticker(
    @SerialName("file_id") val fileId: String,
    val width: Int,
    val height: Int,
    val thumb: PhotoSize? = null,
    val emoji: String? = null,
    @SerialName("set_name") val setName: String? = null,
    @SerialName("mask_position") val maskPosition: MaskPosition? = null,
    @SerialName("file_size") val fileSize: Int? = null
)