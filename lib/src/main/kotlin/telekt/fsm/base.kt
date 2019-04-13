package telekt.fsm

/**
 * You are able to save current user's state
 * and data for all steps in states-storage
 */
abstract class BaseStorage {
    /**
     * You have to override this method and use when application shutdowns.
     * Perhaps you would like to save data and etc.
     */
    abstract suspend fun close()

    /** Get current state of user in chat. Return `default` if no record is found. */
    abstract suspend fun getState(chat: Long, user: Long, default: State? = null): State?

    /** Set new state for user in chat */
    abstract suspend fun setState(chat: Long, user: Long, state: State? = null)

    /**
     * Reset state for user in chat.
     * You may desire to use this method when finishing conversations.
     */
    open suspend fun resetState(chat: Long, user: Long) = setState(chat, user, null)

    /** Finish conversation for user in chat. */
    open suspend fun finish(chat: Long, user: Long): Unit = resetState(chat, user)
}
