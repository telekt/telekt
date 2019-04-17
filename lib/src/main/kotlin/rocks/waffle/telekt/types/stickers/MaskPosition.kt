package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object describes the position on faces where a mask should be placed by default. */
@Serializable data class MaskPosition(
    val point: String,
    @SerialName("x_shift") val xShift: Float,
    @SerialName("y_shift") val yShift: Float,
    val scale: Float
)