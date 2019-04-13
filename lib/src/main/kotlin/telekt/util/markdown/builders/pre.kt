package telekt.util.markdown.builders

import telekt.types.enums.ParseMode
import telekt.util.markdown.hpre
import telekt.util.markdown.mpre


class PreBuilder(parseMode: ParseMode) : AbstractTagBuilder(parseMode) {
    override fun build(): String = if (result == "") result else when (parseMode) {
        ParseMode.HTML -> hpre(result)
        ParseMode.MARKDOWN -> mpre(result)
    }

    companion object {
        inline fun mbuild(block: PreBuilder.() -> Unit): String {
            val bb = PreBuilder(ParseMode.MARKDOWN)
            bb.block()
            return bb.build()
        }

        inline fun hbuild(block: PreBuilder.() -> Unit): String {
            val bb = PreBuilder(ParseMode.HTML)
            bb.block()
            return bb.build()
        }
    }
}