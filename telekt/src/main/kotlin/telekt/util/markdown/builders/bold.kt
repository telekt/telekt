package rocks.waffle.telekt.util.markdown.builders

import rocks.waffle.telekt.types.enums.ParseMode
import rocks.waffle.telekt.util.markdown.hbold
import rocks.waffle.telekt.util.markdown.mbold


class BoldBuilder(parseMode: ParseMode) : AbstractTagBuilder(parseMode) {
    override fun build(): String = if (result == "") result else when (parseMode) {
        ParseMode.HTML -> hbold(result)
        ParseMode.MARKDOWN -> mbold(result)
    }

    companion object {
        inline fun mbuild(block: BoldBuilder.() -> Unit): String {
            val bb = BoldBuilder(ParseMode.MARKDOWN)
            bb.block()
            return bb.build()
        }

        inline fun hbuild(block: BoldBuilder.() -> Unit): String {
            val bb = BoldBuilder(ParseMode.HTML)
            bb.block()
            return bb.build()
        }
    }
}
