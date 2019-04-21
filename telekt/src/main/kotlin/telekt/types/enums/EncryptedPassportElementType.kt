package rocks.waffle.telekt.types.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import rocks.waffle.telekt.util.enumByValueSerializer

@Serializable(with = EncryptedPassportElementType.S::class)
enum class EncryptedPassportElementType(val apiName: String) {
    PERSONAL_DETAILS("personal_details"),
    PASSPORT("passport"),
    DRIVER_LICENSE("driver_license"),
    IDENTITY_CARD("identity_card"),
    INTERNAL_PASSPORT("internal_passport"),
    ADDRESS("address"),
    UTILITY_BILL("utility_bill"),
    BANK_STATEMENT("bank_statement"),
    RENTAL_AGREEMENT("rental_agreement"),
    PASSPORT_REGISTRATION("passport_registration"),
    TEMPORARY_REGISTRATION("temporary_registration"),
    PHONE_NUMBER("phone_number"),
    EMAIL("email");

    @Serializer(forClass = EncryptedPassportElementType::class)
    object S : KSerializer<EncryptedPassportElementType> by enumByValueSerializer({ apiName })
}