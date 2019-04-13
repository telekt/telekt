package telekt.contrib.storages

import telekt.fsm.BaseStorage
import telekt.fsm.State
import java.util.concurrent.ConcurrentHashMap


class ConcurrentHashMapMemoryStorage : BaseStorage() {
    private val fsm: MutableMap<Pair<Long, Long>, State?> = ConcurrentHashMap()

    override suspend fun close() {}
    override suspend fun getState(chat: Long, user: Long, default: State?): State? = fsm[chat to user]
    override suspend fun setState(chat: Long, user: Long, state: State?) {
        fsm[chat to user] = state
    }
}
