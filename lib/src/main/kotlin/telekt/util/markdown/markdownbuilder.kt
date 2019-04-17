package rocks.waffle.telekt.util.markdown

import rocks.waffle.telekt.types.User
import rocks.waffle.telekt.util.markdown.builders.BoldBuilder
import rocks.waffle.telekt.util.markdown.builders.CodeBuilder
import rocks.waffle.telekt.util.markdown.builders.ItalicBuilder
import rocks.waffle.telekt.util.markdown.builders.PreBuilder


class MarkdownTextBuilder : AbstractMdBuilder() {
    //<editor-fold desc="DSL">
    /** escapes this string */
    override operator fun String.not(): String = rocks.waffle.telekt.util.markdown.escapemd(this)
    //</editor-fold>

    /** Make bold text (Markdown) */
    override fun bold(text: String): String = mbold(text)

    /** Make bold text (Markdown) */
    override fun bold(block: BoldBuilder.() -> Unit): String = mbold(block)


    /** Make italic text (Markdown) */
    override fun italic(text: String): String = mitalic(text)

    /** Make italic text (Markdown) */
    override fun italic(block: ItalicBuilder.() -> Unit): String = mitalic(block)


    /** Make mono-width text (Markdown) */
    override fun code(text: String): String = mcode(text)

    /** Make mono-width text (Markdown) */
    override fun code(block: CodeBuilder.() -> Unit): String = mcode(block)


    /** Make mono-width text block (Markdown) */
    override fun pre(text: String): String = mpre(text)

    /** Make mono-width text block (Markdown) */
    override fun pre(block: PreBuilder.() -> Unit): String = mpre(block)


    /** Format URL (Markdown) */
    override fun link(title: String, url: String): String = mlink(title, url)


    /** Format inline mention of a user (Markdown) */
    override fun userLink(title: String, userId: Long): String = mUserLink(title, userId)

    /** Format inline mention of this user (Markdown) */
    override fun User.userLink(title: String) = mUserLink(title)

    /** Format inline mention of this user with his name as title (Markdown) */
    override val User.link: String get() = mlink

    /** Format inline mention of this user with his full name as title (Markdown) */
    override val User.fullNamelink: String get() = mFullNamelink
}
