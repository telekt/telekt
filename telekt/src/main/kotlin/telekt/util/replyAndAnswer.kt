package rocks.waffle.telekt.util

import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.ReplyMarkup
import rocks.waffle.telekt.types.enums.ParseMode

suspend fun Bot.replyTo(
    message: Message,
    text: String,
    parseMode: ParseMode? = null,
    disableWebPagePreview: Boolean? = null,
    disableNotification: Boolean? = null,
    replyMarkup: ReplyMarkup? = null
): Message =
    sendMessage(
        Recipient(message.chat.id),
        text,
        parseMode = parseMode,
        disableWebPagePreview = disableWebPagePreview,
        disableNotification = disableNotification,
        replyToMessageId = message.messageId,
        replyMarkup = replyMarkup
    )

suspend fun Bot.answerOn(
    message: Message,
    text: String,
    parseMode: ParseMode? = null,
    disableWebPagePreview: Boolean? = null,
    disableNotification: Boolean? = null,
    replyToMessageId: Int? = null,
    replyMarkup: ReplyMarkup? = null
): Message =
    sendMessage(
        Recipient(message.chat.id),
        text,
        parseMode = parseMode,
        disableWebPagePreview = disableWebPagePreview,
        disableNotification = disableNotification,
        replyToMessageId = replyToMessageId,
        replyMarkup = replyMarkup
    )

