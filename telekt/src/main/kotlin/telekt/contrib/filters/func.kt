package rocks.waffle.telekt.contrib.filters

import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.types.events.Event

class FuncFilter<T : Event<*>>(val func: suspend (T) -> Boolean) : Filter<T>() {
    override suspend fun test(value: T): Boolean = func(value)
}

fun <T : Event<*>> filter(func: suspend (T) -> Boolean) = FuncFilter(func)
