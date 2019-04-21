package rocks.waffle.telekt.util.markdown.builders

import rocks.waffle.telekt.types.enums.ParseMode
import rocks.waffle.telekt.util.markdown.hitalic
import rocks.waffle.telekt.util.markdown.mitalic


class ItalicBuilder(parseMode: ParseMode) : AbstractTagBuilder(parseMode) {
    override fun build(): String = if (result == "") result else when (parseMode) {
        ParseMode.HTML -> hitalic(result)
        ParseMode.MARKDOWN -> mitalic(result)
    }

    companion object {
        inline fun mbuild(block: ItalicBuilder.() -> Unit): String {
            val bb = ItalicBuilder(ParseMode.MARKDOWN)
            bb.block()
            return bb.build()
        }

        inline fun hbuild(block: ItalicBuilder.() -> Unit): String {
            val bb = ItalicBuilder(ParseMode.HTML)
            bb.block()
            return bb.build()
        }
    }
}