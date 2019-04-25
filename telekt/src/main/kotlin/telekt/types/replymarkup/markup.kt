package rocks.waffle.telekt.types

import kotlinx.serialization.*


// `@Serializable`? (Will it work? Polimorphic serializer?...)
@Serializable(with = ReplyMarkupSerializer::class) sealed class ReplyMarkup

@Serializer(forClass = ReplyMarkup::class) object ReplyMarkupSerializer : KSerializer<ReplyMarkup> {
    override fun serialize(encoder: Encoder, obj: ReplyMarkup) {
        return when (obj) {
            is InlineKeyboardMarkup -> InlineKeyboardMarkup.serializer().serialize(encoder, obj)
            is ReplyKeyboardMarkup -> ReplyKeyboardMarkup.serializer().serialize(encoder, obj)
            is ReplyKeyboardRemove -> ReplyKeyboardRemove.serializer().serialize(encoder, obj)
            is ForceReply -> ForceReply.serializer().serialize(encoder, obj)
        }
    }
}

/** This object represents an inline keyboard that appears right next to the message it belongs to. */
@Serializable /* no data cause of vararg */ class InlineKeyboardMarkup(@SerialName("inline_keyboard") vararg val inlineKeyboard: Array<InlineKeyboardButton>) :
    ReplyMarkup()

/**
 * Upon receiving a message with this object, Telegram clients will remove the current custom keyboard and display the default letter-keyboard.
 * By default, custom keyboards are displayed until a new keyboard is sent by a bot.
 * An exception is made for one-time keyboards that are hidden immediately after the user presses a button (see [ReplyKeyboardMarkup]).
 */
@Serializable data class ReplyKeyboardRemove(val selective: Boolean? = null) : ReplyMarkup() {
    @SerialName("remove_keyboard")
    val removeKeyboard = true
}

/** This object represents a custom keyboard with reply options (see Introduction to bots for details and examples). */
@Serializable /* no data cause of vararg */ class ReplyKeyboardMarkup(
    vararg val keyboard: Array<KeyboardButton>,
    @SerialName("resize_keyboard") val resizeKeyboard: Boolean? = null,
    @SerialName("one_time_keyboard") val oneTimeKeyboard: Boolean? = null,
    val selective: Boolean? = null
) : ReplyMarkup()

/**
 * Upon receiving a message with this object, Telegram clients will display a reply interface to the user
 * (act as if the user has selected the bot‘s message and tapped ’Reply').
 * This can be extremely useful if you want to create user-friendly step-by-step interfaces without having to sacrifice privacy mode.
 */
@Serializable data class ForceReply(
    @SerialName("force_reply") val forceReply: Boolean,
    val selective: Boolean? = null
) : ReplyMarkup()