package rocks.waffle.telekt.util.markdown.builders

import rocks.waffle.telekt.types.enums.ParseMode
import rocks.waffle.telekt.util.markdown.hcode
import rocks.waffle.telekt.util.markdown.mcode


class CodeBuilder(parseMode: ParseMode) : AbstractTagBuilder(parseMode) {
    override fun build(): String = if (result == "") result else when (parseMode) {
        ParseMode.HTML -> hcode(result)
        ParseMode.MARKDOWN -> mcode(result)
    }

    companion object {
        inline fun mbuild(block: CodeBuilder.() -> Unit): String {
            val bb = CodeBuilder(ParseMode.MARKDOWN)
            bb.block()
            return bb.build()
        }

        inline fun hbuild(block: CodeBuilder.() -> Unit): String {
            val bb = CodeBuilder(ParseMode.HTML)
            bb.block()
            return bb.build()
        }
    }
}