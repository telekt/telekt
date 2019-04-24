package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.util.serializer


/**
 * [AnswerCallbackQuery] request.
 * Use this method to send answers to callback queries sent from inline keyboards. The answer will be displayed to the user as a notification at the top of the chat screen or as an alert. On success, True is returned. Alternatively, the user can be redirected to the specified Game URL. For this option to work, you must first create a game for your bot via @Botfather and accept the terms. Otherwise, you may use links like t.me/your_bot?start=XXXX that open your bot with a parameter.
 * More: https://core.telegram.org/bots/api#answercallbackquery
 */
@Serializable data class AnswerCallbackQuery(
    /** Unique identifier for the query to be answered */
    @SerialName("callback_query_id") val callbackQueryId: String,
    /** Text of the notification. If not specified, nothing will be shown to the user, 0-200 characters */
    val text: String? = null,
    /** If true, an alert will be shown by the client instead of a notification at the top of the chat screen. Defaults to false. */
    @SerialName("show_alert") val showAlert: Boolean? = null,
    /** URL that will be opened by the user's client. If you have created a Game and accepted the conditions via @Botfather, specify the URL that opens your game – note that this will only work if the query comes from a callback_game button. Otherwise, you may use links like t.me/your_bot?start=XXXX that open your bot with a parameter. */
    val url: String? = null
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.answerCallbackQuery
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(AnswerCallbackQuery.serializer(), this)
}
