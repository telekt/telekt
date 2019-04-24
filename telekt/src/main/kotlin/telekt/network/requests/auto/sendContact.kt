package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.ReplyMarkup
import rocks.waffle.telekt.util.Recipient


/**
 * [SendContact] request.
 * Use this method to send phone contacts. On success, the sent Message is returned.
 * More: https://core.telegram.org/bots/api#sendcontact
 */
@Serializable data class SendContact(
    /** Unique identifier for the target chat or username of the target channel (in the format @channelusername) */
    @SerialName("chat_id") val chatId: Recipient,
    /** Contact's phone number */
    @SerialName("phone_number") val phoneNumber: String,
    /** Contact's first name */
    @SerialName("first_name") val firstName: String,
    /** Contact's last name */
    @SerialName("last_name") val lastName: String? = null,
    /** Additional data about the contact in the form of a vCard, 0-2048 bytes */
    val vcard: String? = null,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @SerialName("disable_notification") val disableNotification: Boolean? = null,
    /** If the message is a reply, ID of the original message */
    @SerialName("reply_to_message_id") val replyToMessageId: Int? = null,
    /** Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove keyboard or to force a reply from the user. */
    @SerialName("reply_markup") val replyMarkup: ReplyMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.sendContact
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(SendContact.serializer(), this)
}
