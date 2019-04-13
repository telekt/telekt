package telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a shipping address. */
@Serializable data class ShippingAddress(
    @SerialName("country_code") val countryCode: String,
    val state: String,
    val city: String,
    @SerialName("street_line1") val streetLine1: String,
    @SerialName("street_line2") val streetLine2: String,
    @SerialName("post_code") val postCode: String
)