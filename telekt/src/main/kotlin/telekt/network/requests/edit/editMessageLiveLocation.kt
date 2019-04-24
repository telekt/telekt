package rocks.waffle.telekt.network.requests.edit

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.InlineKeyboardMarkup
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.util.Recipient
import rocks.waffle.telekt.util.serializer


/**
 * [EditMessageLiveLocation] inline request.
 * Use this method to edit live location messages sent via the bot (for inline bots).
 * A location can be edited until its live_period expires or
 * editing is explicitly disabled by a call to [StopMessageLiveLocationInline].
 * More: https://core.telegram.org/bots/api#editmessagelivelocation
 */
@Serializable data class EditMessageLiveLocationInline(
    /** Identifier of the inline message */
    @SerialName("inline_message_id") val inlineMessageId: String,
    /** Latitude of new location */
    val latitude: Float,
    /** Longitude of new location */
    val longitude: Float,
    /** A JSON-serialized object for a new inline keyboard. */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.editMessageLiveLocation
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(EditMessageLiveLocationInline.serializer(), this)
}


/**
 * [EditMessageLiveLocation] request.
 * Use this method to edit live location messages sent by the bot.
 * A location can be edited until its live_period expires or
 * editing is explicitly disabled by a call to [StopMessageLiveLocation].
 * On success, the edited Message is returned.
 * More: https://core.telegram.org/bots/api#editmessagelivelocation
 */
@Serializable data class EditMessageLiveLocation(
    /**  Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Identifier of the sent message */
    @SerialName("message_id") val messageId: Int,
    /** Latitude of new location */
    val latitude: Float,
    /** Longitude of new location */
    val longitude: Float,
    /** A JSON-serialized object for a new inline keyboard. */
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.editMessageLiveLocation
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(EditMessageLiveLocation.serializer(), this)

}
