package telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.SimpleRequest
import telekt.types.Message
import telekt.types.ReplyMarkup
import telekt.util.Recipient


/**
 * [SendLocation] request.
 * Use this method to send point on the map. On success, the sent Message is returned.
 * More: https://core.telegram.org/bots/api#sendlocation
 */
@Serializable data class SendLocation(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Latitude of the location */
    val latitude: Float,
    /** Longitude of the location */
    val longitude: Float,
    /** Period in seconds for which the location will be updated (see Live Locations, should be between 60 and 86400. */
    @Optional @SerialName("live_period") val livePeriod: Int? = null,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @Optional @SerialName("disable_notification") val disableNotification: Boolean? = null,
    /** If the message is a reply, ID of the original message */
    @Optional @SerialName("reply_to_message_id") val replyToMessageId: Int? = null,
    /** Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user. */
    @Optional @SerialName("reply_markup") val replyMarkup: ReplyMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.sendLocation
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(SendLocation.serializer(), this)
}
