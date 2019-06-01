package rocks.waffle.telekt.types.replymarkup

import kotlinx.serialization.*

// TODO: more verbose comments

/** This object represents one button of an inline keyboard. */
@Serializable(with = InlineKeyboardButtonSerializer::class) sealed class InlineKeyboardButton {
    /** Label text on the button */
    abstract val text: String

    @Serializable data class Url(
        override val text: String,
        /** HTTP or tg:// url to be opened when button is pressed */
        val url: String
    ) : InlineKeyboardButton()


    @Serializable data class CallbackData(
        override val text: String,
        /** Data to be sent in a callback query to the bot when button is pressed, 1-64 bytes */
        @SerialName("callback_data") val callbackData: String
    ) : InlineKeyboardButton()

    @Serializable data class SwitchInlineQuery(
        override val text: String,
        /**
         * If set, pressing the button will prompt the user to select one of their chats,
         * open that chat and insert the bot‘s username and the specified inline query in the input field.
         * Can be empty, in which case just the bot’s username will be inserted.
         *
         * **Note:** This offers an easy way for users to start using your bot in [inline mode](https://core.telegram.org/bots/inline)
         * when they are currently in a private chat with it.
         * Especially useful when combined with [switch_pm…](https://core.telegram.org/bots/api#answerinlinequery) actions –
         * in this case the user will be automatically returned to the chat they switched from, skipping the chat selection screen.
         */
        @SerialName("switch_inline_query") val switchInlineQuery: String
    ) : InlineKeyboardButton()

    @Serializable data class SwitchInlineQueryCurrentChat(
        override val text: String,
        /**
         * If set, pressing the button will insert the bot‘s username and the specified inline query in the current chat's input field.
         * Can be empty, in which case only the bot’s username will be inserted.
         *
         * This offers a quick way for the user to open your bot in inline mode in the same chat –
         * good for selecting something from multiple options.
         */
        @SerialName("switch_inline_query_current_chat") val SwitchInlineIueryCurrentChat: String
    ) : InlineKeyboardButton()

    /** **NOTE:** This type of button **must** always be the first button in the first row. */
    @Serializable data class CallbackGame(
        override val text: String,
        /** Description of the game that will be launched when the user presses the button. */
        @SerialName("callback_game") val callbackGame: rocks.waffle.telekt.types.CallbackGame
    ) : InlineKeyboardButton()

    /** **NOTE:** This type of button **must** always be the first button in the first row. */
    @Serializable data class Pay(
        override val text: String
    ) : InlineKeyboardButton() {
        val pay: Boolean = true
    }
}

@Serializer(forClass = InlineKeyboardButton::class) object InlineKeyboardButtonSerializer : KSerializer<InlineKeyboardButton> {
    override fun serialize(encoder: Encoder, obj: InlineKeyboardButton) = when (obj) {
        is InlineKeyboardButton.Url -> InlineKeyboardButton.Url.serializer().serialize(encoder, obj)
        is InlineKeyboardButton.CallbackData -> InlineKeyboardButton.CallbackData.serializer().serialize(encoder, obj)
        is InlineKeyboardButton.SwitchInlineQuery -> InlineKeyboardButton.SwitchInlineQuery.serializer().serialize(encoder, obj)
        is InlineKeyboardButton.SwitchInlineQueryCurrentChat -> InlineKeyboardButton.SwitchInlineQueryCurrentChat.serializer().serialize(encoder, obj)
        is InlineKeyboardButton.CallbackGame -> InlineKeyboardButton.CallbackGame.serializer().serialize(encoder, obj)
        is InlineKeyboardButton.Pay -> InlineKeyboardButton.Pay.serializer().serialize(encoder, obj)
    }
}
