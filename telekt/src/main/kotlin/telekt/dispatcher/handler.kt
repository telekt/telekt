package rocks.waffle.telekt.dispatcher

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import mu.KLogger
import mu.KotlinLogging



abstract class Filter<T : TelegramEvent> {
    abstract suspend fun test(scope: HandlerScope, value: T): Boolean
}


data class Handler<T : TelegramEvent>(
    val filters: Array<out Filter<T>>,
    val func: suspend HandlerScope.(T) -> Unit,
    val name: String? = null
) {
    private val logger: KLogger = KotlinLogging.logger { }

    private suspend fun Filter<T>.testCatched(scope: HandlerScope, value: T): Boolean = try {
        test(scope, value)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        logger.error { "Cause error in filter $this while testing value $value: $e" }
        false
    }

    /**
     * Test all [filters] of this handler
     *
     * @return true, if all filters passed, else false
     */
    suspend fun test(scope: HandlerScope, value: T): Boolean = coroutineScope {
        val ch = Channel<Boolean>()

        launch {
            val jobs = mutableListOf<Job>()
            filters.forEach {
                jobs.add(
                    launch {
                        ch.send(
                            it.testCatched(scope, value)
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

class Handlers<T : TelegramEvent> {
    private val logger = KotlinLogging.logger {}

    private var handlers: MutableList<Handler<T>> = mutableListOf()

    fun register(vararg filters: Filter<T>, name: String? = null, handler: suspend HandlerScope.(T) -> Unit) {
        handlers.add(Handler(filters, handler, name = name))
    }

    suspend fun notify(scope: HandlerScope, event: T) {
        val handler = testHandlers(scope, event) ?: return // No handler was found, nothing to do

        try {
            handler.func(scope, event)
        } catch (e: Exception) {
            logger.error(e) { "Occurred exception while executing '${handler.name ?: "unknown"}' handler with update {$event}" }
        }
    }

    /**
     * @return first handler witch filters are passed
     */
    private suspend inline fun testHandlers(scope: HandlerScope, update: T): Handler<T>? = supervisorScope {
        val tests = mutableListOf<Deferred<Boolean>>()
        for (handler in handlers) {
            tests.add(
                async {
                    handler.test(scope, update)
                }
            )
        }

        for (i in 0 until tests.size) {
            try {
                val passed = tests[i].await() // tests were passed
                if (passed) {
                    coroutineContext.cancelChildren()
                    return@supervisorScope handlers[i]
                }
            } catch (e: Exception) {
                logger.error(e) {
                    "Occurred exception while executing some of '${handlers[i].name ?: "unknown"}' handler's filters with update {$update}"
                }
            }
        }
        return@supervisorScope null
    }
}
