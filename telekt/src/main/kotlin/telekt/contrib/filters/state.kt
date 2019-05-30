package rocks.waffle.telekt.contrib.filters

import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.dispatcher.HandlerScope
import rocks.waffle.telekt.dispatcher.TelegramEvent
import rocks.waffle.telekt.fsm.State


class StateFilter<T : TelegramEvent>(private val state: State?) : Filter<T>() {
    override suspend fun test(scope: HandlerScope, value: T): Boolean = scope.fsmContext.getState() == state
}