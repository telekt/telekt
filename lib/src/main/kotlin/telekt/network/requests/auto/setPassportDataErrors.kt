package rocks.waffle.telekt.network.requests.auto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import rocks.waffle.telekt.network.TelegramMethod
import rocks.waffle.telekt.network.requests.abstracts.SimpleRequest
import rocks.waffle.telekt.types.passport.PassportElementError
import rocks.waffle.telekt.util.serializer


/**
 * [SetPassportDataErrors] request.
 * Informs a user that some of the Telegram Passport elements they provided contains errors. The user will not be able to re-submit their Passport to you until the errors are fixed (the contents of the field for which you returned the error must change). Returns True on success. Use this if the data submitted by the user doesn't satisfy the standards your service requires for any reason. For example, if a birthday date seems invalid, a submitted document is blurry, a scan shows evidence of tampering, etc. Supply some details in the error message to make sure the user knows how to correct the issues.
 * More: https://core.telegram.org/bots/api#setpassportdataerrors
 */
@Serializable data class SetPassportDataErrors(
    /** User identifier */
    @SerialName("user_id") val userId: Int,
    /** A JSON-serialized array describing the errors */
    val errors: List<PassportElementError>
) : SimpleRequest<Unit>() {
    @Transient override val method = TelegramMethod.setPassportDataErrors
    @Transient override val resultDeserializer: KSerializer<out Unit> = Unit.serializer()
    override fun stringify(json: Json): String = json.stringify(SetPassportDataErrors.serializer(), this)
}