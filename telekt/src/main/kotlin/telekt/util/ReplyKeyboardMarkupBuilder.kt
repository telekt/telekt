package rocks.waffle.telekt.util

import rocks.waffle.telekt.types.replymarkup.KeyboardButton
import rocks.waffle.telekt.types.replymarkup.ReplyKeyboardMarkup


/**
 * Builder for [ReplyKeyboardMarkup].
 *
 * @param resizeKeyboard Requests clients to resize the keyboard vertically for optimal fit
 *   (e.g., make the keyboard smaller if there are just two rows of buttons).
 *   Defaults to false, in which case the custom keyboard is always of the same height as the app's standard keyboard.
 *
 * @param oneTimeKeyboard Requests clients to hide the keyboard as soon as it's been used.
 *   The keyboard will still be available, but clients will automatically display the usual letter-keyboard in the chat –
 *   the user can press a special button in the input field to see the custom keyboard again. Defaults to false.
 *
 * @param selective Use this parameter if you want to show the keyboard to specific users only.
 *   Targets:
 *   1) users that are @mentioned in the text of the [telekt.types.Message] object;
 *   2) if the bot's message is a reply (has reply_to_message_id), sender of the original message.
 *
 *   Example: A user requests to change the bot‘s language, bot replies to the request with a keyboard to select the new language.
 *   Other users in the group don’t see the keyboard.
 *
 * more: https://core.telegram.org/bots/api#replykeyboardmarkup
 */
@Suppress("FunctionName")
fun ReplyKeyboardMarkup(
    resizeKeyboard: Boolean? = null,
    oneTimeKeyboard: Boolean? = null,
    selective: Boolean? = null,
    lineWidth: Int = 3,
    block: ReplyKeyboardMarkupBuilder.() -> Unit
): ReplyKeyboardMarkup =
    ReplyKeyboardMarkupBuilder(resizeKeyboard, oneTimeKeyboard, selective, lineWidth).also(block).build()

class ReplyKeyboardMarkupBuilder(
    private val resizeKeyboard: Boolean? = null,
    private val oneTimeKeyboard: Boolean? = null,
    private val selective: Boolean? = null,
    private val lineWidth: Int = 3
) {
    private val keyboard = mutableListOf<MutableList<KeyboardButton>>()

    /** Add [buttons] */
    fun add(vararg buttons: KeyboardButton): Unit {
        var line = mutableListOf<KeyboardButton>()

        buttons.forEach { button ->
            line.add(button)
            if (line.size >= lineWidth) {
                keyboard.add(line)
                line = mutableListOf()
            }
        }
        if (line.size > 0) keyboard.add(line)
    }

    /** Add line */
    fun line(vararg buttons: KeyboardButton): Unit {
        keyboard.add(buttons.toMutableList())
    }

    /** Insert [button] to the last line*/
    fun insert(button: KeyboardButton): Unit {
        if (keyboard.isNotEmpty() && keyboard.last().size < lineWidth) keyboard.last().add(button)
        else add(button)
    }

    fun build() = ReplyKeyboardMarkup(
        keyboard,
        resizeKeyboard = resizeKeyboard,
        oneTimeKeyboard = oneTimeKeyboard,
        selective = selective
    )

    operator fun KeyboardButton.unaryPlus() = insert(this)
}
