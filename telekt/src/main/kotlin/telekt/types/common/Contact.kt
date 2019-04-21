package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents a phone contact. */
@Serializable data class Contact(
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("first_name") val firstName: String,
    @Optional @SerialName("last_name") val lastName: String? = null,
    @Optional @SerialName("user_id") val userId: Int? = null,
    @Optional val vcard: String? = null
)