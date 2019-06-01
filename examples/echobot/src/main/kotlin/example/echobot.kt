package rocks.waffle.telekt.examples.echobot

import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.contrib.filters.CommandFilter
import rocks.waffle.telekt.dispatcher.Dispatcher
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.util.answerOn


suspend fun main(args: Array<String>) {
    val parsedArgs = args.parse()

    val bot = Bot(parsedArgs.token)
    val dp = Dispatcher(bot)

    dp.messageHandler(CommandFilter("start", "help")) { message: Message ->
        bot.answerOn(message, "Hi there 0/")
    }

    dp.messageHandler { message ->
        bot.answerOn(message, message.text ?: "this message has no text")
    }

    dp.poll()
}

class Args : NoRunCliktCommand() {
    val token: String by argument(name = "token", help = "Token of the bot")
}

fun Array<String>.parse(): Args {
    val args = Args()
    args.parse(this)
    return args
}