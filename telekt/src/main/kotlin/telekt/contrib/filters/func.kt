package rocks.waffle.telekt.contrib.filters

import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.dispatcher.HandlerScope
import rocks.waffle.telekt.dispatcher.TelegramEvent


class FuncFilter<T : TelegramEvent>(val func: suspend HandlerScope.(T) -> Boolean) : Filter<T>() {
    override suspend fun test(scope: HandlerScope, value: T): Boolean = scope.func(value)
}

fun <T : TelegramEvent> filter(func: suspend HandlerScope.(T) -> Boolean): Filter<T> = FuncFilter(func)
