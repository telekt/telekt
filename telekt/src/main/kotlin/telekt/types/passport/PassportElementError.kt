package rocks.waffle.telekt.types.passport

import kotlinx.serialization.*
import rocks.waffle.telekt.util.enumByValueSerializer

@Serializer(forClass = PassportElementError::class) object PassportElementErrorSerializer : KSerializer<PassportElementError> {
    override fun deserialize(decoder: Decoder): PassportElementError {
        throw NotImplementedError()
    }

    override fun serialize(encoder: Encoder, obj: PassportElementError) {
        return when (obj) {
            is PassportElementErrorDataField -> PassportElementErrorDataField.serializer().serialize(encoder, obj)
            is PassportElementErrorFrontSide -> PassportElementErrorFrontSide.serializer().serialize(encoder, obj)
            is PassportElementErrorReverseSide -> PassportElementErrorReverseSide.serializer().serialize(encoder, obj)
            is PassportElementErrorSelfie -> PassportElementErrorSelfie.serializer().serialize(encoder, obj)
            is PassportElementErrorFile -> PassportElementErrorFile.serializer().serialize(encoder, obj)
            is PassportElementErrorTranslationFile -> PassportElementErrorTranslationFile.serializer().serialize(encoder, obj)
            is PassportElementErrorUnspecified -> PassportElementErrorUnspecified.serializer().serialize(encoder, obj)
        }
    }
}

@Serializable sealed class PassportElementError(val source: String) {
    abstract val message: String
}

@Serializable data class PassportElementErrorDataField(
    val type: Type,
    @SerialName("field_name") val fieldName: String,
    @SerialName("data_hash") val dataHash: String,
    override val message: String
) : PassportElementError("data") {
    @Serializable(with = Type.S::class)
    enum class Type(val apiName: String) {
        PERSONAL_DETAILS("personal_details"),
        PASSPORT("passport"),
        DRIVER_LICENSE("driver_license"),
        IDENTITY_CARD("identity_card"),
        INTERNAL_PASSPORT("internal_passport"),
        ADDRESS("address");

        @Serializer(forClass = Type::class)
        object S : KSerializer<Type> by enumByValueSerializer({ apiName })
    }
}

@Serializable data class PassportElementErrorFrontSide(
    val type: Type,
    @SerialName("file_hash") val fileHash: String,
    override val message: String
) : PassportElementError("front_side") {
    @Serializable(with = Type.S::class)
    enum class Type(val apiName: String) {
        PASSPORT("passport"),
        DRIVER_LICENSE("driver_license"),
        IDENTITY_CARD("identity_card"),
        INTERNAL_PASSPORT("internal_passport");

        @Serializer(forClass = Type::class)
        object S : KSerializer<Type> by enumByValueSerializer({ apiName })
    }
}

@Serializable data class PassportElementErrorReverseSide(
    val type: Type,
    @SerialName("file_hash") val fileHash: String,
    override val message: String
) : PassportElementError("reverse_side") {
    @Serializable(with = Type.S::class)
    enum class Type(val apiName: String) {
        DRIVER_LICENSE("driver_license"),
        IDENTITY_CARD("identity_card");

        @Serializer(forClass = Type::class)
        object S : KSerializer<Type> by enumByValueSerializer({ apiName })
    }
}

@Serializable data class PassportElementErrorSelfie(
    val type: Type,
    @SerialName("file_hash") val fileHash: String,
    override val message: String
) : PassportElementError("selfie") {
    @Serializable(with = Type.S::class)
    enum class Type(val apiName: String) {
        PASSPORT("passport"),
        DRIVER_LICENSE("driver_license"),
        IDENTITY_CARD("identity_card"),
        INTERNAL_PASSPORT("internal_passport");

        @Serializer(forClass = Type::class)
        object S : KSerializer<Type> by enumByValueSerializer({ apiName })
    }
}

@Serializable data class PassportElementErrorFile(
    val type: Type,
    @SerialName("file_hash") val fileHash: String,
    override val message: String
) : PassportElementError("files") {
    @Serializable(with = Type.S::class)
    enum class Type(val apiName: String) {
        UTILITY_BILL("utility_bill"),
        BANK_STATEMENT("bank_statement"),
        RENTAL_AGREEMENT("rental_agreement"),
        PASSPORT_REGISTRATION("passport_registration"),
        TEMPORARY_REGISTRATION("temporary_registration");

        @Serializer(forClass = Type::class)
        object S : KSerializer<Type> by enumByValueSerializer({ apiName })
    }
}

@Serializable data class PassportElementErrorTranslationFile(
    val type: Type,
    @SerialName("file_hashes") val fileHashes: List<String>,
    override val message: String
) : PassportElementError("translation_file") {
    @Serializable(with = Type.S::class)
    enum class Type(val apiName: String) {
        PASSPORT("passport"),
        DRIVER_LICENSE("driver_license"),
        IDENTITY_CARD("identity_card"),
        INTERNAL_PASSPORT("internal_passport"),
        UTILITY_BILL("utility_bill"),
        BANK_STATEMENT("bank_statement"),
        RENTAL_AGREEMENT("rental_agreement"),
        PASSPORT_REGISTRATION("passport_registration"),
        TEMPORARY_REGISTRATION("temporary_registration");

        @Serializer(forClass = Type::class)
        object S : KSerializer<Type> by enumByValueSerializer({ apiName })
    }
}

@Serializable data class PassportElementErrorUnspecified(
    val type: String,
    @SerialName("element_hash") val elementHash: String,
    override val message: String
) : PassportElementError("unspecified")
