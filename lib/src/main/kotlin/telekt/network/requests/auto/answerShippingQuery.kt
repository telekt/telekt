package telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.SimpleRequest
import telekt.types.ShippingOption
import telekt.util.serializer


/**
 * [AnswerShippingQuery] request.
 * If you sent an invoice requesting a shipping address and the parameter is_flexible was specified, the Bot API will send an Update with a shipping_query field to the bot. Use this method to reply to shipping queries.
 * More: https://core.telegram.org/bots/api#answershippingquery
 */
@Serializable data class AnswerShippingQuery(
    /** Unique identifier for the query to be answered */
    @SerialName("shipping_query_id") val shippingQueryId: String,
    /** Specify True if delivery to the specified address is possible and False if there are any problems (for example, if delivery to the specified address is not possible) */
    val ok: Boolean,
    /** Required if ok is True. A JSON-serialized array of available shipping options. */
    @Optional @SerialName("shipping_options") val shippingOptions: List<ShippingOption>? = null,
    /** Required if ok is False. Error message in human readable form that explains why it is impossible to complete the order (e.g. "Sorry, delivery to your desired address is unavailable'). Telegram will display this message to the user. */
    @Optional @SerialName("error_message") val errorMessage: String? = null
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.answerShippingQuery
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(AnswerShippingQuery.serializer(), this)
}