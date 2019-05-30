package rocks.waffle.telekt.util

import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.dispatcher.HandlerScope
import rocks.waffle.telekt.dispatcher.TelegramEvent


class NotFilter<T : TelegramEvent>(private val filter: Filter<T>) : Filter<T>() {
    override suspend fun test(scope: HandlerScope, value: T): Boolean = !filter.test(scope, value)
}

operator fun <T : TelegramEvent> Filter<T>.not(): Filter<T> = NotFilter(this)


class OrFilter<T : TelegramEvent>(private val first: Filter<T>, private val second: Filter<T>) : Filter<T>() {
    override suspend fun test(scope: HandlerScope, value: T): Boolean = coroutineScope {
        val ch = Channel<Boolean>()

        launch {
            val jobs = listOf(
                launch {
                    ch.send(first.test(scope, value))
                },
                launch {
                    ch.send(second.test(scope, value))
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

infix fun <T : TelegramEvent> Filter<T>.or(other: Filter<T>): Filter<T> = OrFilter(this, other)
