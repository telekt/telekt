package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import rocks.waffle.telekt.types.events.TelegramEvent

/** This object contains information about an incoming shipping query. */
@Serializable data class ShippingQuery(
    val id: String,
    val from: User,
    @SerialName("invoice_payload") val invoicePayload: String,
    @SerialName("shipping_address") val shippingAddress: ShippingAddress
) : TelegramEvent