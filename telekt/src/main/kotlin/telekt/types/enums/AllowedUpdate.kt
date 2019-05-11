package rocks.waffle.telekt.types.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import rocks.waffle.telekt.util.enumByValueSerializer

@Serializable(with = AllowedUpdate.S::class) enum class AllowedUpdate(val apiName: String) {
    MESSAGE("message"),
    EDITED_MESSAGE("edited_message"),
    CHANNEL_POST("channel_post"),
    EDITED_CHANNEL_POST("edited_channel_post"),
    INLINE_QUERY("inline_query"),
    CHOSEN_INLINE_RESULT("chosen_inline_result"),
    CALLBACK_QUERY("callback_query"),
    SHIPPING_QUERY("shipping_query"),
    PRE_CHECKOUT_QUERY("pre_checkout_query"),
    POLL("poll");

    @Serializer(forClass = AllowedUpdate::class) object S : KSerializer<AllowedUpdate> by enumByValueSerializer({ apiName })
}