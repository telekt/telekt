package telekt.util

import telekt.bot.Bot
import telekt.types.Message
import telekt.types.ReplyMarkup
import telekt.types.enums.ParseMode
import telekt.types.events.Event

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

suspend fun Bot.replyTo(
    messageEvent: Event<Message>,
    text: String,
    parseMode: ParseMode? = null,
    disableWebPagePreview: Boolean? = null,
    disableNotification: Boolean? = null,
    replyMarkup: ReplyMarkup? = null
): Message =
    replyTo(
        messageEvent.update,
        text,
        parseMode = parseMode,
        disableWebPagePreview = disableWebPagePreview,
        disableNotification = disableNotification,
        replyMarkup = replyMarkup
    )


suspend fun Bot.answerOn(
    messageEvent: Event<Message>,
    text: String,
    parseMode: ParseMode? = null,
    disableWebPagePreview: Boolean? = null,
    disableNotification: Boolean? = null,
    replyToMessageId: Int? = null,
    replyMarkup: ReplyMarkup? = null
): Message =
    answerOn(
        messageEvent.update,
        text,
        parseMode = parseMode,
        disableWebPagePreview = disableWebPagePreview,
        disableNotification = disableNotification,
        replyToMessageId = replyToMessageId,
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

