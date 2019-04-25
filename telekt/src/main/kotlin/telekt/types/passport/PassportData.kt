package rocks.waffle.telekt.types.passport

import kotlinx.serialization.Serializable

@Serializable
data class PassportData(
    val data: List<EncryptedPassportElement>,
    val credentials: EncryptedCredentials
)