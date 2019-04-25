package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.util.serializer


/**
 * [AnswerPreCheckoutQuery] request.
 * Once the user has confirmed their payment and shipping details, the Bot API sends the final confirmation in the form of an Update with the field pre_checkout_query. Use this method to respond to such pre-checkout queries.
 * Note: The Bot API must receive an answer within 10 seconds after the pre-checkout query was sent.
 * More: https://core.telegram.org/bots/api#answerprecheckoutquery
 */
@Serializable data class AnswerPreCheckoutQuery(
    /** Unique identifier for the query to be answered */
    @SerialName("pre_checkout_query_id") val preCheckoutQueryId: String,
    /** Specify True if everything is alright (goods are available, etc.) and the bot is ready to proceed with the order. Use False if there are any problems. */
    val ok: Boolean,
    /** Required if ok is False. Error message in human readable form that explains the reason for failure to proceed with the checkout (e.g. "Sorry, somebody just bought the last of our amazing black T-shirts while you were busy filling out your payment details. Please choose a different color or garment!"). Telegram will display this message to the user. */
    @SerialName("error_message") val errorMessage: String? = null
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.answerPreCheckoutQuery
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(AnswerPreCheckoutQuery.serializer(), this)
}