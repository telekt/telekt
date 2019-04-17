package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.ReplyMarkup
import rocks.waffle.telekt.util.Recipient


/**
 * [SendVenue] request.
 * Use this method to send information about a venue. On success, the sent Message is returned.
 * More: https://core.telegram.org/bots/api#sendvenue
 */
@Serializable data class SendVenue(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Latitude of the venue */
    val latitude: Float,
    /** Longitude of the venue */
    val longitude: Float,
    /** Name of the venue */
    val title: String,
    /** Address of the venue */
    val address: String,
    /** Foursquare identifier of the venue */
    @Optional @SerialName("foursquare_id") val foursquareId: String? = null,
    /** Foursquare type of the venue, if known. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.) */
    @Optional @SerialName("foursquare_type") val foursquareType: String? = null,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @Optional @SerialName("disable_notification") val disableNotification: Boolean? = null,
    /** If the message is a reply, ID of the original message */
    @Optional @SerialName("reply_to_message_id") val replyToMessageId: Int? = null,
    /** Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user. */
    @Optional @SerialName("reply_markup") val replyMarkup: ReplyMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.sendVenue
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(SendVenue.serializer(), this)
}
