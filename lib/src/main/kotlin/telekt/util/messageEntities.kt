package telekt.util

import telekt.types.Message
import telekt.types.MessageEntity
import telekt.types.enums.MessageEntityType
import telekt.types.enums.ParseMode
import telekt.util.markdown.*

fun Message.parseEntities(parseMode: ParseMode = ParseMode.HTML): String {
    val text = text ?: caption ?: throw IllegalArgumentException("This message doesn't have any text.")

    val escape = when (parseMode) {
        ParseMode.HTML -> ::escapehtml
        ParseMode.MARKDOWN -> ::escapemd
    }

    if (entities.isNullOrEmpty()) return escape(text)

    var result = ""
    var offset = 0

    entities.sortedBy { it.offset }.forEach { entity: MessageEntity ->
        val entityText = entity.parse(text, parseMode)

        val part = text.slice(offset until entity.offset)
        result += escape(part) + entityText

        offset = entity.offset + entity.length
    }

    result += escape(text.substring(offset))
    return result
}

fun MessageEntity.getText(text: String) = text.slice(offset until offset + length)

fun MessageEntity.parse(text: String, parseMode: ParseMode = ParseMode.HTML): String = when (parseMode) {
    ParseMode.HTML -> hparse(text)
    ParseMode.MARKDOWN -> mparse(text)
}

private fun MessageEntity.hparse(text: String): String {
    val entityText = getText(text)

    return when (type) {
        MessageEntityType.BOLD -> hbold(entityText)
        MessageEntityType.ITALIC -> hitalic(entityText)
        MessageEntityType.PRE -> hpre(entityText)
        MessageEntityType.CODE -> hcode(entityText)
        MessageEntityType.URL -> hlink(entityText, entityText)
        MessageEntityType.TEXT_LINK -> hlink(entityText, url!!)
        MessageEntityType.TEXT_MENTION -> user?.hUserLink(entityText) ?: entityText
        else -> entityText
    }
}

private fun MessageEntity.mparse(text: String): String {
    val entityText = getText(text)

    return when (type) {
        MessageEntityType.BOLD -> mbold(entityText)
        MessageEntityType.ITALIC -> mitalic(entityText)
        MessageEntityType.PRE -> mpre(entityText)
        MessageEntityType.CODE -> mcode(entityText)
        MessageEntityType.URL -> mlink(entityText, entityText)
        MessageEntityType.TEXT_LINK -> mlink(entityText, url!!)
        MessageEntityType.TEXT_MENTION -> user?.mUserLink(entityText) ?: entityText
        else -> entityText
    }
}