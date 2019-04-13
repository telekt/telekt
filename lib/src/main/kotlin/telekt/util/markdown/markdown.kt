package telekt.util.markdown

import telekt.types.Message
import telekt.types.User
import telekt.types.enums.ParseMode
import telekt.util.fullName
import telekt.util.markdown.builders.BoldBuilder
import telekt.util.markdown.builders.CodeBuilder
import telekt.util.markdown.builders.ItalicBuilder
import telekt.util.markdown.builders.PreBuilder
import telekt.util.parseEntities

private const val LIST_MD_SYMBOLS = "*_`["

private val HTML_QUOTES_MAP = mapOf(
    '<' to "&lt;",
    '>' to "&gt;",
    '&' to "&amp;",
    '"' to "&quot;"
)

private val HQS = HTML_QUOTES_MAP.keys // HQS for HTML QUOTES SYMBOLS

/** Same as [escapehtml] but for Markdown */
fun escapemd(text: String): String = text.map { if (it in LIST_MD_SYMBOLS) "\\" + it else it.toString() }.joinToString("")

/**
 * Quote HTML symbols
 * All <, >, & and " symbols that are not a part of a tag or
 * an HTML entity must be replaced with the corresponding HTML entities
 * (< with &lt; > with &gt; & with &amp and " with &quot).
 */
fun escapehtml(text: String): String = text.map { if (it in HQS) HTML_QUOTES_MAP[it] else it.toString() }.joinToString("")


/** Make bold text (Markdown) */
fun mbold(text: String): String = "*$text*"

/** Make bold text (Markdown) */
inline fun mbold(block: BoldBuilder.() -> Unit): String = BoldBuilder.mbuild(block)

/** Make bold text (HTML) */
fun hbold(text: String): String = "<b>$text</b>"

/** Make bold text (HTML) */
inline fun hbold(block: BoldBuilder.() -> Unit): String = BoldBuilder.hbuild(block)


/** Make italic text (Markdown) */
fun mitalic(text: String): String = "_${text}_"

/** Make italic text (Markdown) */
inline fun mitalic(block: ItalicBuilder.() -> Unit): String = ItalicBuilder.mbuild(block)

/** Make italic text (HTML) */
fun hitalic(text: String): String = "<i>$text</i>"

/** Make italic text (HTML) */
inline fun hitalic(block: ItalicBuilder.() -> Unit): String = ItalicBuilder.hbuild(block)


/** Make mono-width text (Markdown) */
fun mcode(text: String): String = "`$text`"

/** Make mono-width text (Markdown) */
inline fun mcode(block: CodeBuilder.() -> Unit): String = CodeBuilder.mbuild(block)

/** Make mono-width text (HTML) */
fun hcode(text: String): String = "<code>$text</code>"

/** Make mono-width text (HTML) */
inline fun hcode(block: CodeBuilder.() -> Unit): String = CodeBuilder.hbuild(block)


/** Make mono-width text block (Markdown) */
fun mpre(text: String): String = "```$text```"

/** Make mono-width text block (Markdown) */
inline fun mpre(block: PreBuilder.() -> Unit): String = PreBuilder.mbuild(block)

/** Make mono-width text (HTML) */
fun hpre(text: String): String = "<pre>$text</pre>"

/** Make mono-width text (HTML) */
inline fun hpre(block: PreBuilder.() -> Unit): String = PreBuilder.hbuild(block)


/** Format URL (Markdown) */
fun mlink(title: String, url: String): String = "[$title]($url)"

/** Format URL (HTML) */
fun hlink(title: String, url: String): String = "<a href=\"$url\">$title</a>"


/** Format inline mention of a user (Markdown) */
fun mUserLink(title: String, userId: Long): String = mlink(title, "tg://user?id=$userId")

/** Format inline mention of a user (HTML) */
fun hUserLink(title: String, userId: Long): String = hlink(title, "tg://user?id=$userId")


/** Format inline mention of this user (Markdown) */
fun User.mUserLink(title: String) = mUserLink(title, id)

/** Format inline mention of this user (HTML) */
fun User.hUserLink(title: String) = hUserLink(title, id)


/** Format inline mention of this user with his name as title (Markdown) */
inline val User.mlink: String get() = mUserLink(firstName)

/** Format inline mention of this user with his name as title (HTML) */
inline val User.hlink: String get() = hUserLink(firstName)

/** Format inline mention of this user with his full name as title (Markdown) */
inline val User.mFullNamelink: String get() = mUserLink(fullName)

/** Format inline mention of this user with his full name as title (HTML) */
inline val User.hFullNamelink: String get() = hUserLink(fullName)


/** Hide URL (HTML only) */
fun hideLink(url: String) = hlink(url, "&#8203;")


/** Parses message text & entities to correct Markdown formatted text */
inline val Message.mdText: String get() = parseEntities(ParseMode.MARKDOWN)

/** Parses message text & entities to correct HTML formatted text */
inline val Message.htmlText: String get() = parseEntities(ParseMode.HTML)


/** Markdown 'DSL' */
fun markdown(block: MarkdownTextBuilder.() -> Unit): String {
    val builder = MarkdownTextBuilder()
    builder.block()
    return builder.build()
}

/** HTML 'DSL' */
fun html(block: HtmlTextBuilder.() -> Unit): String {
    val builder = HtmlTextBuilder()
    builder.block()
    return builder.build()
}

/** Joins all strings together */
fun join(vararg strings: String): String = strings.joinToString("")
