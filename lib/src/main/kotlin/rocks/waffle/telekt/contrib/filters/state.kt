package rocks.waffle.telekt.contrib.filters

import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.fsm.State
import rocks.waffle.telekt.types.events.Event

class StateFilter<T : Event<*>>(private val state: State?) : Filter<T>() {
    override suspend fun test(value: T): Boolean = value.fsmContext.getState() == state
}