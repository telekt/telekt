package telekt.network.requests.auto

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import telekt.network.TelegramMethod
import telekt.network.requests.abstracts.SimpleRequest
import telekt.types.Message
import telekt.types.ReplyMarkup
import telekt.util.Recipient


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
    @Optional @SerialName("last_name") val lastName: String? = null,
    /** Additional data about the contact in the form of a vCard, 0-2048 bytes */
    @Optional val vcard: String? = null,
    /** Sends the message silently. Users will receive a notification with no sound. */
    @Optional @SerialName("disable_notification") val disableNotification: Boolean? = null,
    /** If the message is a reply, ID of the original message */
    @Optional @SerialName("reply_to_message_id") val replyToMessageId: Int? = null,
    /** Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove keyboard or to force a reply from the user. */
    @Optional @SerialName("reply_markup") val replyMarkup: ReplyMarkup? = null
) : SimpleRequest<Message>() {
    @Transient override val method = TelegramMethod.sendContact
    @Transient override val resultDeserializer: KSerializer<out Message> = Message.serializer()
    override fun stringify(json: Json): String = json.stringify(SendContact.serializer(), this)
}
