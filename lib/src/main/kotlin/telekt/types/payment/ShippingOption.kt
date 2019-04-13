package telekt.types

import kotlinx.serialization.Serializable

/** This object represents one shipping option. */
@Serializable data class ShippingOption(
    val id: String,
    val title: String,
    val prices: Array<LabeledPrice>
)