package rocks.waffle.telekt.types.passport

import kotlinx.serialization.Serializable

@Serializable class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String
)