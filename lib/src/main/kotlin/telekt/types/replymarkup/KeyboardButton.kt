package telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents one button of the reply keyboard. For simple text buttons ChannelUsername can be used instead of this object to specify text of the button. Optional fields are mutually exclusive. */
@Serializable data class KeyboardButton(
    val text: String,
    @Optional @SerialName("request_contact") val requestContact: Boolean? = null,
    @Optional @SerialName("request_location") val requestLocation: Boolean? = null
)