package rocks.waffle.telekt.contrib.storages

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import rocks.waffle.telekt.fsm.BaseStorage
import rocks.waffle.telekt.fsm.State
import kotlin.coroutines.CoroutineContext

data class ChatUserKey(val chat: Long, val user: Long)

typealias Key = ChatUserKey

private sealed class FsmAction {
    data class GetState(val key: Key, val result: CompletableDeferred<State?> = CompletableDeferred()) : FsmAction()
    data class SetState(val key: Key, val state: State?, val result: CompletableDeferred<Unit> = CompletableDeferred()) : FsmAction()
}

class ActorMemoryStorage : CoroutineScope, BaseStorage() {
    private val job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Default + job

    val fsmMap = mutableMapOf<Key, State?>()


    @kotlinx.coroutines.ObsoleteCoroutinesApi
    private val fsmActor = actor<FsmAction> {
        consumeEach { action ->
            when (action) {
                is FsmAction.GetState -> action.result.complete(fsmMap[action.key])
                is FsmAction.SetState -> {
                    fsmMap[action.key] = action.state
                    action.result.complete(Unit)
                }
            }
        }
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    override suspend fun close() {
        fsmActor.close()
        job.join()
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    override suspend fun getState(chat: Long, user: Long, default: State?): State? {
        val action = FsmAction.GetState(Key(chat, user))
        fsmActor.send(action)
        return action.result.await()
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    override suspend fun setState(chat: Long, user: Long, state: State?) {
        val action = FsmAction.SetState(Key(chat, user), state)
        fsmActor.send(action)
        action.result.await()
    }
}
