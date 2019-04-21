package rocks.waffle.telekt.fsm

/**
 * Get current state of user in private messages. Return `default` if no record is found.
 *
 * In channels, there is no 'user id'.
 * So if [user] is not provided, [getState] will return state for channel. (equal to `getState(chat, chat)`)
 *
 * In private conversation with user, 'chat id' == 'user id'.
 * So if [chat] is not provided, [getState] will return state for user, in his private messages with bot. (equal to `getState(user, user)`)
 *
 * @throws IllegalArgumentException if doth [chat] and [user] are null
 */
suspend fun BaseStorage.getState(chat: Long? = null, user: Long? = null, default: State? = null): State? {
    val (chat_, user_) = guess(chat, user)
    return getState(chat_, user_, default)
}

/**
 * Set new state for user in private messages.
 *
 * In channels, there is no 'user id'.
 * So if [user] is not provided, [setState] will set state for channel. (equal to `setState(chat, chat)`)
 *
 * In private conversation with user, 'chat id' == 'user id'.
 * So if [chat] is not provided, [setState] will set state for user, in his private messages with bot. (equal to `setState(user, user)`)
 *
 * @throws IllegalArgumentException if doth [chat] and [user] are null
 */
suspend fun BaseStorage.setState(chat: Long? = null, user: Long? = null, state: State? = null): Unit {
    val (chat_, user_) = guess(chat, user)
    setState(chat_, user_, state)
}

/**
 * Reset state for user in private messages.
 * You may desire to use this method when finishing conversations.
 *
 * In channels, there is no 'user id'.
 * So if [user] is not provided, [resetState] will reset state for channel. (equal to `resetState(chat, chat)`)
 *
 * In private conversation with user, 'chat id' == 'user id'.
 * So if [chat] is not provided, [resetState] will reset state for user, in his private messages with bot. (equal to `resetState(user, user)`)
 *
 * @throws IllegalArgumentException if doth [chat] and [user] are null
 */
suspend fun BaseStorage.resetState(chat: Long? = null, user: Long? = null): Unit {
    val (chat_, user_) = guess(chat, user)
    resetState(chat_, user_)
}

/**
 * Finish conversation for user in private messages.
 *
 * In channels, there is no 'user id'.
 * So if [user] is not provided, [finish] will finish "conversation" in channel channel. (equal to `finish(chat, chat)`)
 *
 * In private conversation with user, 'chat id' == 'user id'.
 * So if [chat] is not provided, [finish] will finish conversation with user, in his private messages with bot. (equal to `finish(user, user)`)
 *
 * @throws IllegalArgumentException if doth [chat] and [user] are null
 */
suspend fun BaseStorage.finish(chat: Long? = null, user: Long? = null): Unit {
    val (chat_, user_) = guess(chat, user)
    finish(chat_, user_)
}


private data class Guess(val chat: Long, val user: Long)

private fun guess(chat: Long? = null, user: Long? = null): Guess {
    if (chat == null && user == null) throw IllegalArgumentException(
        "At least one of chat & user must be NOT null, for guessing address for FSM storage"
    )

    return Guess(chat ?: user!!, chat ?: user!!)
}