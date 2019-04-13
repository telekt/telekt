package telekt.examples.echobot

import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import telekt.bot.Bot
import telekt.contrib.filters.CommandFilter
import telekt.dispatcher.Dispatcher
import telekt.types.events.MessageEvent
import telekt.types.events.message
import telekt.util.answerOn


suspend fun main(args: Array<String>) {
    val parsedArgs = args.parse()

    val bot = Bot(parsedArgs.token)
    val dp = Dispatcher(bot)

    dp.messageHandler(CommandFilter("start", "help")) { event: MessageEvent ->
        bot.answerOn(event, "Hi there 0/")
    }

    dp.messageHandler { event ->
        bot.answerOn(event, event.message.text ?: "this message has no text")
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