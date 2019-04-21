package rocks.waffle.telekt.types.passport

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import rocks.waffle.telekt.types.enums.EncryptedPassportElementType

@Serializable
class EncryptedPassportElement(
    val type: EncryptedPassportElementType,
    @Optional val data: String? = null,
    @SerialName("phone_number") @Optional val phoneNumber: String? = null,
    @Optional val email: String? = null,
    @Optional val files: List<PassportFile>? = null,
    @SerialName("front_side") @Optional val frontSide: PassportFile? = null,
    @SerialName("reverse_side") @Optional val reverseSide: PassportFile? = null,
    @Optional val selfie: PassportFile? = null,
    @Optional val translation: List<PassportFile>? = null,
    val hash: String
)