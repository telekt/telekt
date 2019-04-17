package rocks.waffle.telekt.util.markdown

import rocks.waffle.telekt.types.User
import rocks.waffle.telekt.util.markdown.builders.BoldBuilder
import rocks.waffle.telekt.util.markdown.builders.CodeBuilder
import rocks.waffle.telekt.util.markdown.builders.ItalicBuilder
import rocks.waffle.telekt.util.markdown.builders.PreBuilder

abstract class AbstractMdBuilder {
    private var result: String = ""

    //<editor-fold desc="DSL">
    /** appends [text] to result */
    open fun append(text: String) {
        result += text
    }

    /** appends this string to result */
    open operator fun String.unaryPlus(): Unit = append(this)

    /** escapes this string */
    abstract operator fun String.not(): String

    /** appends new line to text*/
    fun nl(): Unit = append("\n")
    //</editor-fold>

    /** Make bold text */
    abstract fun bold(text: String): String

    /** Make bold text */
    abstract fun bold(block: BoldBuilder.() -> Unit): String


    /** Make italic text */
    abstract fun italic(text: String): String

    /** Make italic text */
    abstract fun italic(block: ItalicBuilder.() -> Unit): String


    /** Make mono-width text */
    abstract fun code(text: String): String

    /** Make mono-width text */
    abstract fun code(block: CodeBuilder.() -> Unit): String


    /** Make mono-width text block */
    abstract fun pre(text: String): String

    /** Make mono-width text block */
    abstract fun pre(block: PreBuilder.() -> Unit): String


    /** Format URL */
    abstract fun link(title: String, url: String): String


    /** Format inline mention of a user */
    abstract fun userLink(title: String, userId: Long): String

    /** Format inline mention of this user */
    abstract fun User.userLink(title: String): String

    /** Format inline mention of this user with his name as title */
    abstract val User.link: String

    /** Format inline mention of this user with his full name as title */
    abstract val User.fullNamelink: String


    fun build(): String = result
}
