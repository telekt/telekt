package telekt.util.markdown

import telekt.types.User
import telekt.util.markdown.builders.BoldBuilder
import telekt.util.markdown.builders.CodeBuilder
import telekt.util.markdown.builders.ItalicBuilder
import telekt.util.markdown.builders.PreBuilder


class HtmlTextBuilder : AbstractMdBuilder() {
    private var result: String = ""

    //<editor-fold desc="DSL">
    /** escapes this string */
    override operator fun String.not(): String = telekt.util.markdown.escapehtml(this)
    //</editor-fold>

    /** Make bold text (Markdown) */
    override fun bold(text: String): String = hbold(text)

    /** Make bold text (Markdown) */
    override fun bold(block: BoldBuilder.() -> Unit): String = hbold(block)


    /** Make italic text (Markdown) */
    override fun italic(text: String): String = hitalic(text)

    /** Make italic text (Markdown) */
    override fun italic(block: ItalicBuilder.() -> Unit): String = hitalic(block)


    /** Make mono-width text (Markdown) */
    override fun code(text: String): String = hcode(text)

    /** Make mono-width text (Markdown) */
    override fun code(block: CodeBuilder.() -> Unit): String = hcode(block)


    /** Make mono-width text block (Markdown) */
    override fun pre(text: String): String = hpre(text)

    /** Make mono-width text block (Markdown) */
    override fun pre(block: PreBuilder.() -> Unit): String = hpre(block)


    /** Format URL (Markdown) */
    override fun link(title: String, url: String): String = hlink(title, url)


    /** Format inline mention of a user (HTML) */
    override fun userLink(title: String, userId: Long): String = hUserLink(title, userId)

    /** Format inline mention of this user (HTML) */
    override fun User.userLink(title: String): String = hUserLink(title)

    /** Format inline mention of this user with his name as title (HTML) */
    override val User.link: String get() = hlink

    /** Format inline mention of this user with his full name as title (HTML) */
    override val User.fullNamelink: String get() = hFullNamelink
}
