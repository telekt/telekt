package telekt.util.markdown.builders

import telekt.types.enums.ParseMode
import telekt.util.markdown.escapehtml
import telekt.util.markdown.escapemd

abstract class AbstractTagBuilder(val parseMode: ParseMode) {
    protected var result: String = ""

    //<editor-fold desc="DSL">
    /** appends [text] to result */
    fun append(text: String) {
        result += text
    }

    /** appends this string to result */
    operator fun String.unaryPlus(): Unit = append(this)

    /** escapes this string */
    operator fun String.not(): String = when (parseMode) {
        ParseMode.HTML -> escapehtml(this)
        ParseMode.MARKDOWN -> escapemd(this)
    }

    /** appends new line to text*/
    fun nl(): Unit = append("\n")
    //</editor-fold>

    abstract fun build(): String
}

