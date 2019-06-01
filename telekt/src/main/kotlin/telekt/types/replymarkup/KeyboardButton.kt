package rocks.waffle.telekt.types.replymarkup

import kotlinx.serialization.*

// TODO: more verbose comments

/**
 * This object represents one button of the reply keyboard.
 * For simple text buttons ChannelUsername can be used instead of this object to specify text of the button.
 * Optional fields are mutually exclusive.
 */
@Serializable(with = KeyboardButtonSerializer::class) sealed class KeyboardButton {
    /** Text of the button. */
    abstract val text: String

    @Serializable data class Text(override val text: String) : KeyboardButton()

    @Serializable data class RequestContact(
        override val text: String
    ) : KeyboardButton() {
        /** If True, the user's phone number will be sent as a contact when the button is pressed. Available in private chats only */
        @SerialName("request_contact") val requestContact: Boolean = true
    }

    @Serializable data class RequestLocation(
        override val text: String
    ) : KeyboardButton() {
        /** If True, the user's current location will be sent when the button is pressed. Available in private chats only */
        @SerialName("request_location") val requestLocation: Boolean = true
    }
}

/**
 * Alias for [KeyboardButton.Text]
 *
 * @param text Text of the button
 */
@Suppress("FunctionName")
fun KeyboardButton(text: String): KeyboardButton = KeyboardButton.Text(text)

@Serializer(forClass = KeyboardButton::class) object KeyboardButtonSerializer : KSerializer<KeyboardButton> {
    override fun serialize(encoder: Encoder, obj: KeyboardButton) = when (obj) {
        is KeyboardButton.Text -> KeyboardButton.Text.serializer().serialize(encoder, obj)
        is KeyboardButton.RequestContact -> KeyboardButton.RequestContact.serializer().serialize(encoder, obj)
        is KeyboardButton.RequestLocation -> KeyboardButton.RequestLocation.serializer().serialize(encoder, obj)
    }
}
