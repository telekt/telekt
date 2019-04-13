package telekt.fsm

interface State {
    val id: String

    fun next(): State = throw NotImplementedError()
    fun prev(): State = throw NotImplementedError()
}
