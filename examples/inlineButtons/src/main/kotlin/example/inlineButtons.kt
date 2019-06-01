package rocks.waffle.telekt.examples.inlineButtons

import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.dispatcher.Dispatcher
import rocks.waffle.telekt.types.replymarkup.InlineKeyboardButton
import rocks.waffle.telekt.util.InlineKeyboardMarkup
import rocks.waffle.telekt.util.answerOn
import rocks.waffle.telekt.util.handlerregistration.*
import rocks.waffle.telekt.util.replyTo


suspend fun main(args: Array<String>) {
    val (token) = args.parse()

    val bot = Bot(token)
    val dp = Dispatcher(bot)

    dp.dispatch {
        messages {
            handle(command("start")) {
                bot.answerOn(it, it.text!!)
            }

            handle {
                val murkup = InlineKeyboardMarkup {
                    // buttons with callbackData will send callback query update to bot
                    +InlineKeyboardButton.CallbackData("none", "none")
                    +InlineKeyboardButton.CallbackData("text", "text")
                    +InlineKeyboardButton.CallbackData("alert", "alert")
                    +InlineKeyboardButton.CallbackData("url-call", "url")

                    // buttons with url will just redirect user to passed url
                    +InlineKeyboardButton.Url("url", url = "https://example.com")
                }

                bot.replyTo(it, "inline keyboard:", replyMarkup = murkup)
            }
        }

        callbackQuerys {
            /*
             * NOTE: some telegram clients can send callback query with changed callback data.
             * So, you always need to check if user can tap on this button
             */

            // text filter can be also used to check call back data
            handle(text("none")) { call ->
                // You need to always answer callback query to hide 'clock' on client's side
                bot.answerCallbackQuery(call.id)
            }

            handle(text("text")) { call ->
                // will show text on the top of the screen
                bot.answerCallbackQuery(call.id, text = "hello there!")
            }

            handle(text("alert")) { call ->
                // will show alert
                bot.answerCallbackQuery(call.id, text = "hello, alert!", showAlert = true)
            }

            handle(text("url")) { call ->
                // you can pass there game url (see: https://core.telegram.org/bots/api#answercallbackquery)
                //
                // Or link to your bot with start parameter
                // (called also 'deep linking' see https://core.telegram.org/bots#deep-linking):
                bot.answerCallbackQuery(call.id, url = "t.me/${bot.me.await().username}?start=TEST")
            }
        }
    }

    dp.poll()
}

class Args : NoRunCliktCommand() {
    val token: String by argument(name = "token", help = "Token of the bot")
    operator fun component1(): String = token
}

fun Array<String>.parse(): Args {
    val args = Args()
    args.parse(this)
    return args
}