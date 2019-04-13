package telekt.util

import telekt.exceptions.TokenValidationException
import telekt.types.PhotoSize
import telekt.types.User

val User.fullName: String
    get() = if (lastName == null) firstName else firstName + lastName

// Is this the best way to check is ChannelUsername - Digit? >_<
fun String.isDigit() = toIntOrNull() != null

fun String.hasWhiteSpaces() = this.any { it.isWhitespace() }

/**
 * Validate Bot token
 *
 * Token is always invalid if:
 * - If contains more on less than one ":"
 * - If part before ":" is not digit
 * - It length of part after ":" < 3
 *
 * @param token bot token from @BotFather (t.me/BotFather)
 * @throws TokenValidationException if [throwErrors] is true and token is invalid
 * @return true if token [ may be ] correct else - false
 */
fun checkToken(token: String, throwErrors: Boolean = false): Boolean {
    val split = token.split(':')

    if (split.size != 2 || token.hasWhiteSpaces() || !split[0].isDigit() || split[1].length < 3) {
        if (throwErrors) throw TokenValidationException()
        return false
    }
    return true
}

fun Array<PhotoSize>.maxSize(): PhotoSize = last()
fun Array<PhotoSize>.minSize(): PhotoSize = first()