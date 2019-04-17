package rocks.waffle.telekt.types

import kotlinx.serialization.Optional
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import rocks.waffle.telekt.types.events.TelegramEvent

/** This object represents an incoming update. At most one of the optional parameters can be present in any given update. */
@Serializable data class Update(
    @SerialName("update_id") val updateId: Int,
    @Optional val message: Message? = null,
    @Optional @SerialName("edited_message") val editedMessage: Message? = null,
    @Optional @SerialName("channel_post") val channelPost: Message? = null,
    @Optional @SerialName("edited_channel_post") val editedChannelPost: Message? = null,
    @Optional @SerialName("inline_query") val inlineQuery: InlineQuery? = null,
    @Optional @SerialName("chosen_inline_result") val chosenInlineResult: ChosenInlineResult? = null,
    @Optional @SerialName("callback_query") val callbackQuery: CallbackQuery? = null,
    @Optional @SerialName("shipping_query") val shippingQuery: ShippingQuery? = null,
    @Optional @SerialName("pre_checkout_query") val preCheckoutQuery: PreCheckoutQuery? = null
) : TelegramEvent
