package rocks.waffle.telekt.dispatcher

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import mu.KLogger
import mu.KotlinLogging
import rocks.waffle.telekt.types.events.*


abstract class Filter<T : Event<*>> {
    abstract suspend fun test(value: T): Boolean
}


data class Handler<T : Event<*>>(
    val filters: Array<out Filter<T>>,
    val func: suspend (T) -> Unit,
    val name: String? = null
) {
    private val logger: KLogger = KotlinLogging.logger { }

    private suspend fun Filter<T>.testCatched(value: T): Boolean {
        return try {
            test(value)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.error { "Cause error in filter $this while testing value $value: $e" }
            false
        }
    }

    /**
     * Test all [filters] of this handler
     *
     * @return true, if all filters passed, else false
     */
    suspend fun test(value: T): Boolean = coroutineScope {
        val ch = Channel<Boolean>()

        launch {
            val jobs = mutableListOf<Job>()
            filters.forEach {
                jobs.add(
                    launch {
                        ch.send(
                            it.testCatched(value)
                        )
                    }
                )
            }

            jobs.joinAll()
            ch.close()
        }

        for (data in ch) {
            if (!data) {
                coroutineContext.cancelChildren() // to kill the rest of tasks (filter tests) we  don't need
                return@coroutineScope false
            }
        }
        return@coroutineScope true
    }
}

class Handlers<T : Event<*>> {
    private val logger = KotlinLogging.logger {}

    private var handlers: MutableList<Handler<T>> = mutableListOf()

    fun register(vararg filters: Filter<T>, name: String? = null, handler: suspend (T) -> Unit) {
        handlers.add(Handler(filters, handler, name = name))
    }

    fun unregister(name: String): Boolean = handlers.removeIf { it.name == name }

    suspend fun notify(event: T) = coroutineScope {
        val handler = testHandlers(event) ?: return@coroutineScope // No handler was finded, nothing to do

        try {
            handler.func(event)
        } catch (e: Exception) {
            logger.error(e) { "Occurred exception while executing '${handler.name ?: "unknown"}' handler with update {$event}" }
        }
    }

    /**
     * @return first handler witch filters are passed
     */
    private suspend inline fun testHandlers(update: T): Handler<T>? = coroutineScope {
        val tests = mutableListOf<Deferred<Boolean>>()
        for (handler in handlers) {
            tests.add(
                async {
                    handler.test(update)
                }
            )
        }

        for (i in 0..(tests.size - 1)) {
            try {
                val passed = tests[i].await() // tests were passed
                if (passed) {
                    coroutineContext.cancelChildren()
                    return@coroutineScope handlers[i]
                }
            } catch (e: Exception) {
                logger.error(e) {
                    "Occurred exception while executing some of '${handlers[i].name ?: "unknown"}' handler's filters with update {$update}"
                }
            }
        }
        return@coroutineScope null
    }
}
