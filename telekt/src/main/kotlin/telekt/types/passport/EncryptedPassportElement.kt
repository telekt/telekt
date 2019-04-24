package rocks.waffle.telekt.types.passport

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import rocks.waffle.telekt.types.enums.EncryptedPassportElementType

@Serializable
data class EncryptedPassportElement(
    val type: EncryptedPassportElementType,
    val data: String? = null,
    @SerialName("phone_number") val phoneNumber: String? = null,
    val email: String? = null,
    val files: List<PassportFile>? = null,
    @SerialName("front_side") val frontSide: PassportFile? = null,
    @SerialName("reverse_side") val reverseSide: PassportFile? = null,
    val selfie: PassportFile? = null,
    val translation: List<PassportFile>? = null,
    val hash: String
)