package rocks.waffle.telekt.types

import kotlinx.serialization.*


// `@Serializable`? (Will it work? Polimorphic serializer?...)
/** This object represents the content of a message to be sent as a result of an inline query. Telegram clients currently support the following 4 types */
@Serializable(with = InputMessageContentSerializer::class) sealed class InputMessageContent


@Serializer(forClass = InputMessageContent::class) object InputMessageContentSerializer : KSerializer<InputMessageContent> {
    override fun deserialize(decoder: Decoder): InputMessageContent {
        throw NotImplementedError()
    }

    override fun serialize(encoder: Encoder, obj: InputMessageContent) = when (obj) {
            is InputContactMessageContent -> InputContactMessageContent.serializer().serialize(encoder, obj)
            is InputLocationMessageContent -> InputLocationMessageContent.serializer().serialize(encoder, obj)
            is InputTextMessageContent -> InputTextMessageContent.serializer().serialize(encoder, obj)
            is InputVenueMessageContent -> InputVenueMessageContent.serializer().serialize(encoder, obj)
    }

}

/** Represents the content of a contact message to be sent as the result of an inline query. */
@Serializable data class InputContactMessageContent(
    @SerialName("phone_number") val phoneNumber: String,
    @SerialName("first_name") val firstName: String,
    @Optional @SerialName("last_name") val lastName: String? = null,
    @Optional val vcard: String? = null
) : InputMessageContent()


/** Represents the content of a location message to be sent as the result of an inline query. */
@Serializable data class InputLocationMessageContent(
    val latitude: Float,
    val longitude: Float,
    @Optional @SerialName("live_period") val livePeriod: Int? = null
) : InputMessageContent()


/** Represents the content of a text message to be sent as the result of an inline query. */
@Serializable data class InputTextMessageContent(
    @SerialName("message_text") val messageText: String,
    @Optional @SerialName("parse_mode") val parseMode: String? = null,
    @Optional @SerialName("disable_web_page_preview") val disableWebPagePreview: Boolean? = null
) : InputMessageContent()


/** Represents the content of a venue message to be sent as the result of an inline query. */
@Serializable data class InputVenueMessageContent(
    val latitude: Float,
    val longitude: Float,
    val title: String,
    val address: String,
    @Optional @SerialName("foursquare_id") val foursquareId: String? = null,
    @Optional @SerialName("foursquare_type") val foursquareType: String? = null
) : InputMessageContent()