package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.replymarkup.ReplyMarkup
import rocks.waffle.telekt.util.Recipient


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
    @SerialName("live_period") val livePeriod: Int? = null,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @SerialName("disable_notification") val disableNotification: Boolean? = null,
    /** If the message is a reply, ID of the original message */
    @SerialName("reply_to_message_id") val replyToMessageId: Int? = null,
    /** Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user. */
    @SerialName("reply_markup") val replyMarkup: ReplyMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.sendLocation
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(SendLocation.serializer(), this)
}
