package rocks.waffle.telekt.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents one button of an inline keyboard. You must use exactly one of the optional fields. */
@Serializable data class InlineKeyboardButton(
    val text: String,
    val url: String? = null,
    @SerialName("callback_data") val callbackData: String? = null,
    @SerialName("switch_inline_query") val switchInlineQuery: String? = null,
    @SerialName("switch_inline_query_current_chat") val switchInlineQueryCurrentChat: String? = null,
    @SerialName("callback_game") val callbackGame: CallbackGame? = null,
    val pay: Boolean? = null
)