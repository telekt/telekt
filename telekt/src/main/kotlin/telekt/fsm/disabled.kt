package rocks.waffle.telekt.fsm

import mu.KotlinLogging

/**
 * Empty storage. Use it if you don't want to use Finite-State Machine
 */
object DisabledStorage : BaseStorage() {
    private val logger = KotlinLogging.logger {}

    override suspend fun close() {}

    override suspend fun getState(chat: Long, user: Long, default: State?): State? = null

    override suspend fun setState(chat: Long, user: Long, state: State?) = logger.warn(
        "You haven’t set any storage yet so no states will be saved. \n" +
                "You can connect ActorMemoryStorage or ConcurrentHashMapMemoryStorage for debug purposes or non-essential data."
    )
}
