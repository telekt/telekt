package rocks.waffle.telekt.util

import rocks.waffle.telekt.types.replymarkup.InlineKeyboardButton
import rocks.waffle.telekt.types.replymarkup.InlineKeyboardMarkup


/**
 * Builder for [InlineKeyboardMarkup].
 *
 * more: https://core.telegram.org/bots/api#inlinekeyboardmarkup
 */
@Suppress("FunctionName")
fun InlineKeyboardMarkup(
    lineWidth: Int = 4,
    block: InlineKeyboardMarkupBuilder.() -> Unit
): InlineKeyboardMarkup =
    InlineKeyboardMarkupBuilder(lineWidth).also(block).build()

class InlineKeyboardMarkupBuilder(val lineWidth: Int = 4) {
    private val keyboard = mutableListOf<MutableList<InlineKeyboardButton>>()

    /** Add [buttons] */
    fun add(vararg buttons: InlineKeyboardButton): Unit {
        var line = mutableListOf<InlineKeyboardButton>()

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
    fun line(vararg buttons: InlineKeyboardButton): Unit {
        keyboard.add(buttons.toMutableList())
    }

    /** Insert [button] to the last line*/
    fun insert(button: InlineKeyboardButton): Unit {
        if (keyboard.isNotEmpty() && keyboard.last().size < lineWidth) keyboard.last().add(button)
        else add(button)
    }

    fun build() = InlineKeyboardMarkup(keyboard)

    operator fun InlineKeyboardButton.unaryPlus() = insert(this)
}
