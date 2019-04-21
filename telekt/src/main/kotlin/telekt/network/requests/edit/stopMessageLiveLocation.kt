package rocks.waffle.telekt.network.requests.edit

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.util.Recipient
import rocks.waffle.telekt.util.serializer


/**
 * [StopMessageLiveLocation] inline request.
 * Use this method to stop updating a live location message sent via the bot (for inline bots) before live_period expires.
 * More: https://core.telegram.org/bots/api#stopmessagelivelocation
 */
@Serializable data class StopMessageLiveLocationInline(
    /** Identifier of the inline message */
    @SerialName("inline_message_id") val inlineMessageId: String
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.stopMessageLiveLocation
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(StopMessageLiveLocationInline.serializer(), this)
}


/**
 * [StopMessageLiveLocation] request.
 * Use this method to stop updating a live location message sent by the bot before live_period expires.
 * On success, the sent Message is returned.
 * More: https://core.telegram.org/bots/api#stopmessagelivelocation
 */
@Serializable data class StopMessageLiveLocation(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Identifier of the sent message */
    @SerialName("message_id") val messageId: Int
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.stopMessageLiveLocation
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(StopMessageLiveLocation.serializer(), this)
}
