package telekt.fsm


class FSMContext(val storage: BaseStorage, val chat: Long, val user: Long) {
    suspend inline fun getState(default: State? = null): State? = storage.getState(chat, user, default)

    suspend inline fun setState(state: State? = null) = storage.setState(chat, user, state)

    suspend inline fun resetState() = storage.resetState(chat, user)

    suspend inline fun finish() = storage.finish(chat, user)
}

suspend fun FSMContext.next(): Unit {
    getState()?.next()?.let { setState(it) }
}