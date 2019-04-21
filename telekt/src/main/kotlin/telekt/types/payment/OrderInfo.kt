package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents information about an order. */
@Serializable data class OrderInfo(
    @Optional val name: String? = null,
    @Optional @SerialName("phone_number") val phoneNumber: String? = null,
    @Optional val email: String? = null,
    @Optional @SerialName("shipping_address") val shippingAddress: ShippingAddress? = null
)