package rocks.waffle.telekt.examples.dslechobot

import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.dispatcher.Dispatcher
import rocks.waffle.telekt.util.answerOn
import rocks.waffle.telekt.util.handlerregistration.command
import rocks.waffle.telekt.util.handlerregistration.dispatch
import rocks.waffle.telekt.util.handlerregistration.handle
import rocks.waffle.telekt.util.handlerregistration.messages


suspend fun main(args: Array<String>) {
    val (token) = args.parse()

    val bot = Bot(token)
    val dp = Dispatcher(bot)

    dp.dispatch {
        messages {
            handle(command("start", "help")) { (message) ->
                bot.answerOn(message, "Hi there 0/")
            }

            handle { (message) ->
                bot.answerOn(message, message.text ?: "this message has no text")
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