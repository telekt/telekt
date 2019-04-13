package telekt.util.markdown.builders

import telekt.types.enums.ParseMode
import telekt.util.markdown.hcode
import telekt.util.markdown.mcode


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