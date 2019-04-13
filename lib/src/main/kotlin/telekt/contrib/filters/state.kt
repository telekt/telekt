package telekt.contrib.filters

import telekt.dispatcher.Filter
import telekt.fsm.State
import telekt.types.events.Event

class StateFilter<T : Event<*>>(private val state: State?) : Filter<T>() {
    override suspend fun test(value: T): Boolean = value.fsmContext.getState() == state
}