package telekt.types

import kotlinx.serialization.Serializable

/** This object represents a portion of the price for goods or services. */
@Serializable data class LabeledPrice(
    val label: String,
    val amount: Int
)