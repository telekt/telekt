package telekt.util

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import telekt.bot.Bot
import telekt.dispatcher.Filter
import telekt.fsm.DisabledStorage
import telekt.types.CallbackQuery
import telekt.types.User
import telekt.types.events.CallbackQueryEvent
import telekt.types.events.Event

class NotFilter<E>(private val filter: Filter<E>) : Filter<E>() where E : Event<*> {
    override suspend fun test(value: E): Boolean = !filter.test(value)
}

operator fun <E> Filter<E>.not(): Filter<E> where E : Event<*> = NotFilter(this)


class OrFilter<E>(private val first: Filter<E>, private val second: Filter<E>) : Filter<E>() where E : Event<*> {
    override suspend fun test(value: E): Boolean = coroutineScope {
        val ch = Channel<Boolean>()

        launch {
            val jobs = listOf(
                launch {
                    ch.send(first.test(value))
                },
                launch {
                    ch.send(second.test(value))
                }
            )

            jobs.joinAll()
            ch.close()
        }

        for (data in ch) {
            if (data) {
                coroutineContext.cancelChildren() // to kill the rest of tasks (filter tests) we don't need
                return@coroutineScope true
            }
        }
        return@coroutineScope false
    }
}

infix fun <E> Filter<E>.or(other: Filter<E>): Filter<E> where E : Event<*> = OrFilter(this, other)
