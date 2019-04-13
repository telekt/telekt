package telekt.exceptions

import org.reflections.Reflections
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.kotlinFunction


internal inline fun <reified T : Annotation, E : TelegramAPIException> getExceptions(
    reflections: Reflections,
    match: T.() -> String
): MutableList<Pair<KFunction<E>, String>> {
    val result: MutableList<Pair<KFunction<E>, String>> = mutableListOf()
    reflections.getTypesAnnotatedWith(T::class.java).forEach { clazz ->
        clazz.annotations.forEach { ann ->
            if (ann is T) {
                result.add(clazz.constructors.first().kotlinFunction as KFunction<E> to ann.match())
            }
        }
    }
    return result
}

internal object ReflectionsObj {
    val reflections by lazy { Reflections("telekt.exceptions") }
}

@Target(AnnotationTarget.CLASS)
annotation class ErrorTextConflict(val match: String)

@Target(AnnotationTarget.CLASS)
annotation class ErrorTextNotFound(val match: String)

@Target(AnnotationTarget.CLASS)
annotation class ErrorTextUnauthorized(val match: String)

@Target(AnnotationTarget.CLASS)
annotation class ErrorTextBadRequest(val match: String)

abstract class Detector<T> {
    abstract val exceptions: MutableList<Pair<KFunction<T>, String>>
    fun detect(description: String) = detect(description, exceptions as List<Pair<KFunction<TelegramAPIException>, String>>)
}

val PREFIXES = arrayOf("Error: ", "[Error]: ", "Bad Request: ", "Conflict: ", "Not Found: ")

/**
 * Remove `PREFIXES`, trim and capitalize message
 * @return 'clear' message
 */
private fun String.cleanMessage(): String {
    var result = this
    for (prefix in PREFIXES) {
        result = result.removePrefix(prefix)
    }
    return result.trim().capitalize()
}

class UnknownTelegramException(descr: String) : TelegramAPIException("Unknown telegram exception with description \"$descr\".\n Please connect to lib maintainer.")

/** Base exception of all telegram exceptions */
open class TelegramAPIException(message: String) : Exception(message.cleanMessage())

/**
 * Detect Exception by description
 *
 * @param description description of exception from telegram
 * @param exceptions possible exceptions
 */
fun detect(description: String, exceptions: List<Pair<KFunction<TelegramAPIException>, String>>): TelegramAPIException {
    for ((constr, match) in exceptions) {
        if (match.toLowerCase() in description.toLowerCase()) {
            return constr.call(description)
        }
    }
    throw UnknownTelegramException(description)
}


class TokenValidationException : Exception("Token is invalid!")

class PollingWasAlreadyStopped : Exception("Polling was already stopped")

class NetworkError(message: String) : TelegramAPIException(message)

class RestartingTelegram : TelegramAPIException("The Telegram Bot API service is restarting. Wait few second.")

class RetryAfter(val retryAfter: Int) : TelegramAPIException("Flood control exceeded. Retry in $retryAfter seconds.")

class MigrateToChat(val migrateToChatId: Long) :
    TelegramAPIException("The group has been migrated to a supergroup. New id: $migrateToChatId.")
