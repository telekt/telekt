package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a sticker set. */
@Serializable data class StickerSet(
    val name: String,
    val title: String,
    @SerialName("contains_masks") val containsMasks: Boolean,
    val stickers: Array<Sticker>
)