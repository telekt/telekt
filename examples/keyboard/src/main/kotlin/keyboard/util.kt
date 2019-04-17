package rocks.waffle.telekt.examples.keyboard

import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument


class Args : NoRunCliktCommand() {
    val token: String by argument(name = "token", help = "Token of the bot")
}

fun Array<String>.parse(): Args {
    val args = Args()
    args.parse(this)
    return args
}
