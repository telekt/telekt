package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import rocks.waffle.telekt.types.events.TelegramEvent

/** This object contains information about an incoming pre-checkout query. */
@Serializable data class PreCheckoutQuery(
    val id: String,
    val from: User,
    val currency: String,
    @SerialName("total_amount") val totalAmount: Int,
    @SerialName("invoice_payload") val invoicePayload: String,
    @Optional @SerialName("shipping_option_id") val shippingOptionId: String? = null,
    @Optional @SerialName("order_info") val orderInfo: OrderInfo? = null
) : TelegramEvent