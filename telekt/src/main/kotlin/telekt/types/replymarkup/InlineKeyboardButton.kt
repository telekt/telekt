package rocks.waffle.telekt.types.replymarkup

import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import rocks.waffle.telekt.types.CallbackGame

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

    @Serializable data class LoginUrl(
        override val text: String,
        /**
         * An HTTP URL used to automatically authorize the user.
         * Can be used as a replacement for the [Telegram Login Widget](https://core.telegram.org/widgets/login).
         */
        @SerialName("login_url") val loginUrl: rocks.waffle.telekt.types.replymarkup.LoginUrl
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
        @SerialName("switch_inline_query_current_chat") val SwitchInlineQueryCurrentChat: String
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
        is InlineKeyboardButton.LoginUrl -> InlineKeyboardButton.LoginUrl.serializer().serialize(encoder, obj)
        is InlineKeyboardButton.CallbackData -> InlineKeyboardButton.CallbackData.serializer().serialize(encoder, obj)
        is InlineKeyboardButton.SwitchInlineQuery -> InlineKeyboardButton.SwitchInlineQuery.serializer().serialize(encoder, obj)
        is InlineKeyboardButton.SwitchInlineQueryCurrentChat -> InlineKeyboardButton.SwitchInlineQueryCurrentChat.serializer().serialize(encoder, obj)
        is InlineKeyboardButton.CallbackGame -> InlineKeyboardButton.CallbackGame.serializer().serialize(encoder, obj)
        is InlineKeyboardButton.Pay -> InlineKeyboardButton.Pay.serializer().serialize(encoder, obj)
    }

    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("ReplyMarkup") {
        init {
            addElement("text") // index0
            addElement("url") // index1
            addElement("login_url") // index2
            addElement("callback_data") // index3
            addElement("switch_inline_query") // index4
            addElement("switch_inline_query_current_chat") // index5
            addElement("callback_game") // index6
            addElement("pay") // index7
        }
    }


    override fun deserialize(decoder: Decoder): InlineKeyboardButton {
        val inp: CompositeDecoder = decoder.beginStructure(descriptor)

        var text: String? = null
        var url: String? = null
        var loginUrl: LoginUrl? = null
        var callbackData: String? = null
        var switchInlineQuery: String? = null
        var switchInlineQueryCurrentChat: String? = null
        var callbackGame: CallbackGame? = null
        var pay: Boolean? = null

        loop@ while (true) {
            when (val i = inp.decodeElementIndex(descriptor)) {
                CompositeDecoder.READ_DONE -> break@loop
                0 -> text = inp.decodeStringElement(descriptor, i)
                1 -> url = inp.decodeStringElement(descriptor, i)
                2 -> loginUrl = inp.decodeSerializableElement(descriptor, i, LoginUrl.serializer())
                3 -> callbackData = inp.decodeStringElement(descriptor, i)
                4 -> switchInlineQuery = inp.decodeStringElement(descriptor, i)
                5 -> switchInlineQueryCurrentChat = inp.decodeStringElement(descriptor, i)
                6 -> callbackGame = inp.decodeSerializableElement(descriptor, i, CallbackGame.serializer())
                7 -> pay = inp.decodeBooleanElement(descriptor, i)

                else -> throw SerializationException("Unknown index $i")
            }
        }
        inp.endStructure(descriptor)

        if (text == null) throw SerializationException("text must be given")

        return when {
            url != null -> InlineKeyboardButton.Url(text, url)
            loginUrl != null -> InlineKeyboardButton.LoginUrl(text, loginUrl)
            callbackData != null -> InlineKeyboardButton.CallbackData(text, callbackData)
            switchInlineQuery != null -> InlineKeyboardButton.SwitchInlineQuery(text, switchInlineQuery)
            switchInlineQueryCurrentChat != null -> InlineKeyboardButton.SwitchInlineQueryCurrentChat(text, switchInlineQueryCurrentChat)
            callbackGame != null -> InlineKeyboardButton.CallbackGame(text, callbackGame)
            pay != null -> InlineKeyboardButton.Pay(text)
            else -> throw SerializationException("WTF?")
        }
    }
}
