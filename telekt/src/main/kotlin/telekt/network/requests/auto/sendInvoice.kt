package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.InlineKeyboardMarkup
import rocks.waffle.telekt.types.LabeledPrice
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.util.Recipient


/**
 * [SendInvoice] request.
 * Use this method to send invoices.
 * On success, the sent Message is returned.
 * More: https://core.telegram.org/bots/api#sendinvoice
 */
@Serializable data class SendInvoice(
    /** Unique identifier for the target private chat */
    @SerialName("chat_id") val chatId: Recipient,
    /** Product name, 1-32 characters */
    val title: String,
    /** Product description, 1-255 characters */
    val description: String,
    /** Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your internal processes. */
    val payload: String,
    /** Payments provider token, obtained via Botfather */
    @SerialName("provider_token") val providerToken: String,
    /** Unique deep-linking parameter that can be used to generate this invoice when used as a start parameter */
    @SerialName("start_parameter") val startParameter: String,
    /** Three-letter ISO 4217 currency code, see more on currencies */
    val currency: String,
    /** Price breakdown, a list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.) */
    val prices: List<LabeledPrice>,
    /** JSON-encoded data about the invoice, which will be shared with the payment provider. A detailed description of required fields should be provided by the payment provider. */
    @SerialName("provider_data") val providerData: String? = null,
    /** URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service. People like it better when they see what they are paying for. */
    @SerialName("photo_url") val photoUrl: String? = null,
    /** Photo size */
    @SerialName("photo_size") val photoSize: Int? = null,
    /** Photo width */
    @SerialName("photo_width") val photoWidth: Int? = null,
    /** Photo height */
    @SerialName("photo_height") val photoHeight: Int? = null,
    /** Pass True, if you require the user's full name to complete the order */
    @SerialName("need_name") val needName: Boolean? = null,
    /** Pass True, if you require the user's phone number to complete the order */
    @SerialName("need_phone_number") val needPhoneNumber: Boolean? = null,
    /** Pass True, if you require the user's email address to complete the order */
    @SerialName("need_email") val needEmail: Boolean? = null,
    /** Pass True, if you require the user's shipping address to complete the order */
    @SerialName("need_shipping_address") val needShippingAddress: Boolean? = null,
    /** Pass True, if user's phone number should be sent to provider */
    @SerialName("send_phone_number_to_provider") val sendPhoneNumberToProvider: Boolean? = null,
    /** Pass True, if user's email address should be sent to provider */
    @SerialName("send_email_to_provider") val sendEmailToProvider: Boolean? = null,
    /** Pass True, if the final price depends on the shipping method */
    @SerialName("is_flexible") val isFlexible: Boolean? = null,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @SerialName("disable_notification") val disableNotification: Boolean? = null,
    /** If the message is a reply, ID of the original message */
    @SerialName("reply_to_message_id") val replyToMessageId: Int? = null,
    /** A JSON-serialized object for an inline keyboard. If empty, one 'Pay total price' button will be shown. If not empty, the first button must be a Pay button. */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.sendInvoice
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(SendInvoice.serializer(), this)
}