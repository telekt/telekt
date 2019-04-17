package rocks.waffle.telekt.examples.fsm

import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import rocks.waffle.telekt.contrib.filters.filter
import rocks.waffle.telekt.fsm.State
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.events.Event
import rocks.waffle.telekt.util.handlerregistration.HandlerDSL
import rocks.waffle.telekt.util.isDigit


/** Is digit filter */
val <E> @Suppress("unused") HandlerDSL<E>.isDigit where E : Event<Message> get() = filter<E> { (m) -> m.text?.isDigit() ?: false }

enum class States(override val id: String) : State {
    NAME("name") {
        override fun next(): State = AGE
    },
    AGE("age") {
        override fun next(): State = GENDER
    },
    GENDER("gender")
}

class Args : NoRunCliktCommand() {
    val token: String by argument(name = "token", help = "Token of the bot")
}

fun Array<String>.parse(): Args {
    val args = Args()
    args.parse(this)
    return args
}
