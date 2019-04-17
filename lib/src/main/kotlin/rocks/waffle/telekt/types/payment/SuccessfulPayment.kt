package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object contains basic information about a successful payment. */
@Serializable data class SuccessfulPayment(
    val currency: String,
    @SerialName("total_amount") val totalAmount: Int,
    @SerialName("invoice_payload") val invoicePayload: String,
    @Optional @SerialName("shipping_option_id") val shippingOptionId: String? = null,
    @Optional @SerialName("order_info") val orderInfo: OrderInfo? = null,
    @SerialName("telegram_payment_charge_id") val telegramPaymentChargeId: String,
    @SerialName("provider_payment_charge_id") val providerPaymentChargeId: String
)