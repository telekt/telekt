package rocks.waffle.telekt.dispatcher

import com.kizitonwose.time.seconds
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import mu.KotlinLogging
import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.exceptions.PollingWasAlreadyStarted
import rocks.waffle.telekt.exceptions.PollingWasAlreadyStopped
import rocks.waffle.telekt.exceptions.WebhookWasAlreadyStarted
import rocks.waffle.telekt.exceptions.WebhookWasAlreadyStopped
import rocks.waffle.telekt.fsm.BaseStorage
import rocks.waffle.telekt.fsm.DisabledStorage
import rocks.waffle.telekt.types.Update
import rocks.waffle.telekt.types.enums.AllowedUpdate
import rocks.waffle.telekt.types.events.*
import java.util.concurrent.TimeUnit


class KtorDispatcher(
    val bot: Bot,
    private val storage: BaseStorage = DisabledStorage,
    val webhookConfig: WebhookConfig = WebhookConfig()
) : Dispatcher {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    data class WebhookConfig(val server: ApplicationEngineFactory<*, *> = Netty)

    private val logger = KotlinLogging.logger {}

    private var lastUpdateId: Int = 0

    private val updateHandlers: Handlers<UpdateEvent> by lazy {
        val handlers = Handlers<UpdateEvent>()
        handlers.register(name = "INTERNAL_UPDATE_HANDLER") {
            processUpdate(it.update)
        }
        handlers
    }

    //<editor-fold desc="handlers">
    private val messageHandlers: Handlers<MessageEvent> = Handlers()
    private val editedMessageHandlers: Handlers<EditedMessageEvent> = Handlers()
    private val channelPostHandlers: Handlers<ChannelPostEvent> = Handlers()
    private val editedChannelPostHandlers: Handlers<EditedChannelPostEvent> = Handlers()
    private val inlineQueryHandlers: Handlers<InlineQueryEvent> = Handlers()
    private val chosenInlineResultHandlers: Handlers<ChosenInlineResultEvent> = Handlers()
    private val callbackQueryHandlers: Handlers<CallbackQueryEvent> = Handlers()
    private val shippingQueryHandlers: Handlers<ShippingQueryEvent> = Handlers()
    private val preCheckoutQueryHandlers: Handlers<PreCheckoutQueryEvent> = Handlers()
    private val pollHandlers: Handlers<PollEvent> = Handlers()
    //</editor-fold>

    //<editor-fold desc="handlers-registration">
    override fun messageHandler(
        vararg filters: Filter<MessageEvent>,
        name: String?,
        block: suspend (MessageEvent) -> Unit
    ): Unit =
        messageHandlers.register(*filters, handler = block, name = name)

    override fun editedMessageHandler(
        vararg filters: Filter<EditedMessageEvent>,
        name: String?,
        block: suspend (EditedMessageEvent) -> Unit
    ): Unit =
        editedMessageHandlers.register(*filters, handler = block, name = name)

    override fun channelPostHandler(
        vararg filters: Filter<ChannelPostEvent>,
        name: String?,
        block: suspend (ChannelPostEvent) -> Unit
    ): Unit =
        channelPostHandlers.register(*filters, handler = block, name = name)

    override fun editedChannelPostHandler(
        vararg filters: Filter<EditedChannelPostEvent>,
        name: String?,
        block: suspend (EditedChannelPostEvent) -> Unit
    ): Unit =
        editedChannelPostHandlers.register(*filters, handler = block, name = name)

    override fun inlineQueryHandler(
        vararg filters: Filter<InlineQueryEvent>,
        name: String?,
        block: suspend (InlineQueryEvent) -> Unit
    ): Unit =
        inlineQueryHandlers.register(*filters, handler = block, name = name)

    override fun chosenInlineResultHandler(
        vararg filters: Filter<ChosenInlineResultEvent>,
        name: String?,
        block: suspend (ChosenInlineResultEvent) -> Unit
    ): Unit =
        chosenInlineResultHandlers.register(*filters, handler = block, name = name)

    override fun callbackQueryHandler(
        vararg filters: Filter<CallbackQueryEvent>,
        name: String?,
        block: suspend (CallbackQueryEvent) -> Unit
    ): Unit =
        callbackQueryHandlers.register(*filters, handler = block, name = name)

    override fun shippingQueryHandler(
        vararg filters: Filter<ShippingQueryEvent>,
        name: String?,
        block: suspend (ShippingQueryEvent) -> Unit
    ): Unit =
        shippingQueryHandlers.register(*filters, handler = block, name = name)

    override fun preCheckoutQueryHandler(
        vararg filters: Filter<PreCheckoutQueryEvent>,
        name: String?,
        block: suspend (PreCheckoutQueryEvent) -> Unit
    ): Unit =
        preCheckoutQueryHandlers.register(*filters, handler = block, name = name)

    override fun pollHandler(
        vararg filters: Filter<PollEvent>,
        name: String?,
        block: suspend (PollEvent) -> Unit): Unit =
        pollHandlers.register(*filters, handler = block, name = name)
    //</editor-fold>

    override suspend fun skipUpdates() {
        bot.getUpdates(-1, timeout = 1)
        logger.info("Skipped all updates")
    }

    override suspend fun processUpdates(updates: List<Update>) = coroutineScope {
        for (update in updates) {
            launch {
                updateHandlers.notify(
                    UpdateEvent(update, bot, storage)
                )
            }
        }
    }

    /** Process single update object */
    private suspend fun processUpdate(update: Update) {
        lastUpdateId = update.updateId

        update.run {
            when {
                message != null -> messageHandlers.notify(MessageEvent(message, bot, storage))
                editedMessage != null -> editedMessageHandlers.notify(EditedMessageEvent(editedMessage, bot, storage))
                channelPost != null -> channelPostHandlers.notify(ChannelPostEvent(channelPost, bot, storage))
                editedChannelPost != null -> editedChannelPostHandlers.notify(EditedChannelPostEvent(editedChannelPost, bot, storage))
                inlineQuery != null -> inlineQueryHandlers.notify(InlineQueryEvent(inlineQuery, bot, storage))
                chosenInlineResult != null -> chosenInlineResultHandlers.notify(ChosenInlineResultEvent(chosenInlineResult, bot, storage))
                callbackQuery != null -> callbackQueryHandlers.notify(CallbackQueryEvent(callbackQuery, bot, storage))
                shippingQuery != null -> shippingQueryHandlers.notify(ShippingQueryEvent(shippingQuery, bot, storage))
                preCheckoutQuery != null -> preCheckoutQueryHandlers.notify(PreCheckoutQueryEvent(preCheckoutQuery, bot, storage))
                poll != null -> pollHandlers.notify(PollEvent(poll, bot, storage))
            }
        }
    }

    //<editor-fold desc="long polling">
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    override suspend fun poll(
        relax: Float,
        resetWebhook: Boolean?,
        timeout: Int,
        limit: Byte?,
        allowedUpdates: List<AllowedUpdate>?,
        wait: Boolean
    ): Unit = polling.start(relax, resetWebhook, timeout, limit, allowedUpdates, wait)

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    override suspend fun stopPolling(wait: Boolean): Unit = polling.stop(wait)


    private sealed class PollingSignal {
        data class Start(
            val relax: Float,
            val resetWebhook: Boolean?,
            val timeout: Int,
            val limit: Byte?,
            val allowedUpdates: List<AllowedUpdate>?,
            val exception: CompletableDeferred<Exception?>,
            val stopCallback: CompletableDeferred<Unit>
        ) : PollingSignal()

        data class Stop(
            val exception: CompletableDeferred<Exception?>,
            val operationEndCallback: CompletableDeferred<Unit>
        ) : PollingSignal()

        data class Close(
            val operationEndCallback: CompletableDeferred<Unit>
        ) : PollingSignal()
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    private inner class Polling {
        private val pollingControlActor = scope.actor<PollingSignal>(onCompletion = { println("actor complete!") }) {
            var stopCallback: CompletableDeferred<Unit>? = null
            var job: Job? = null

            loop@ for (signal in channel) when (signal) {

                // Start the http server
                is PollingSignal.Start -> {
                    if (job != null) {
                        signal.exception.complete(PollingWasAlreadyStarted)
                        // actually, polling doesn't stop, but we return exception, so it's OK
                        signal.stopCallback.complete(Unit)
                    } else {
                        signal.exception.complete(null)
                        stopCallback = signal.stopCallback

                        job = getPollingJob(signal.relax, signal.resetWebhook, signal.timeout, signal.limit, signal.allowedUpdates)
                        logger.info("Start polling")
                    }
                }

                // Stop the long polling
                is PollingSignal.Stop -> {
                    if (job == null) {
                        val (exception, end) = signal
                        exception.complete(PollingWasAlreadyStopped)
                        end.complete(Unit)
                    } else {
                        val (exception, end) = signal
                        exception.complete(null)
                        job.cancelAndJoin()
                        logger.warn("Stop polling")
                        stopCallback?.complete(Unit)
                        end.complete(Unit)
                    }
                }

                // Stop long polling, close actor & end work
                // Should be called ONLY from `KtorDispatcher.close()`
                is PollingSignal.Close -> {
                    val (end) = signal
                    job?.cancelAndJoin()
                    logger.warn("Stop polling")
                    stopCallback?.complete(Unit)
                    this@actor.channel.close()
                    end.complete(Unit)
                    break@loop
                }
            }
        }

        fun CoroutineScope.getPollingJob(
            relax: Float,
            resetWebhook: Boolean?,
            timeout: Int,
            limit: Byte?,
            allowedUpdates: List<AllowedUpdate>?
        ): Job = launch {
            var offset: Int? = null

            withContext(NonCancellable) {
                while (this@launch.isActive) {
                    val updates = try {
                        bot.getUpdates(offset = offset, limit = limit, timeout = timeout, allowedUpdates = allowedUpdates)
                    } catch (e: Exception) {
                        logger.error(e) { "Cause exception while getting updates:" }
                        delay(15.seconds.inMilliseconds.longValue)
                        continue
                    }

                    if (updates.isNotEmpty()) {
                        logger.debug { "Received ${updates.size} updates" }
                        offset = updates.last().updateId + 1

                        scope.launch {
                            processUpdates(updates)
                        }
                    }

                    if (relax > 0) delay(relax.seconds.inMilliseconds.longValue)
                }
            }
        }

        suspend fun start(relax: Float, resetWebhook: Boolean?, timeout: Int, limit: Byte?, allowedUpdates: List<AllowedUpdate>?, wait: Boolean) {
            val exception = CompletableDeferred<Exception?>()
            val end = CompletableDeferred<Unit>()

            // send signal for polling starting
            pollingControlActor.send(
                PollingSignal.Start(relax, resetWebhook, timeout, limit, allowedUpdates, exception, end)
            )

            // throw exception if something go wrong
            exception.await()?.let { throw it }

            // wait for the polling to stop
            if (wait) end.await()
        }

        suspend fun stop(wait: Boolean) {
            val exception = CompletableDeferred<Exception?>()
            val end = CompletableDeferred<Unit>()

            // send signal for the polling to stop
            pollingControlActor.send(PollingSignal.Stop(exception, end))

            // throw exception if something go wrong
            exception.await()?.let { throw it }

            // wait for operation to end
            if (wait) end.await()
        }

        suspend fun close() {
            val closeEnd = CompletableDeferred<Unit>()
            pollingControlActor.send(PollingSignal.Close(closeEnd))
            closeEnd.await()
        }
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    private val polling by lazy { Polling() }

    //</editor-fold>

    //<editor-fold desc="webhook">
    private sealed class WebhookSignal {
        data class Start(
            val host: String,
            val port: Int,
            val path: String,
            val exception: CompletableDeferred<Exception?>,
            val stopCallback: CompletableDeferred<Unit>
        ) : WebhookSignal()

        data class Stop(
            val exception: CompletableDeferred<Exception?>,
            val operationEndCallback: CompletableDeferred<Unit>
        ) : WebhookSignal()

        data class Close(
            val operationEndCallback: CompletableDeferred<Unit>
        ) : WebhookSignal()
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    private inner class Webhook {
        private val webhookControlActor = scope.actor<WebhookSignal> {
            var server: ApplicationEngine? = null
            var stopCallback: CompletableDeferred<Unit>? = null
            val json = Json(JsonConfiguration.Stable.copy(encodeDefaults = false, strictMode = false))

            loop@ for (signal in channel) when (signal) {

                // Start the http server
                is WebhookSignal.Start -> {
                    if (server != null) {
                        signal.exception.complete(WebhookWasAlreadyStarted)
                        // actually, server doesn't stop, but we return exception, so it's OK
                        signal.stopCallback.complete(Unit)
                    } else {
                        signal.exception.complete(null)
                        stopCallback = signal.stopCallback

                        server = getServer(port = signal.port, host = signal.host, path = signal.path, json = json).start(wait = false)
                    }
                }

                // Stop the http server
                is WebhookSignal.Stop -> {
                    if (server == null) {
                        val (exception, end) = signal
                        exception.complete(WebhookWasAlreadyStopped)
                        end.complete(Unit)
                    } else {
                        val (exception, end) = signal
                        exception.complete(null)
                        server.stop(5, 5, TimeUnit.SECONDS)
                        server = null
                        stopCallback?.complete(Unit)
                        end.complete(Unit)
                    }
                }

                // Stop the http server, close actor & end work
                // Should be called ONLY from `KtorDispatcher.close()`
                is WebhookSignal.Close -> {
                    val (end) = signal
                    server?.stop(1, 1, TimeUnit.SECONDS)
                    stopCallback?.complete(Unit)
                    this@actor.channel.close()
                    end.complete(Unit)
                    break@loop
                }
            }
        }

        private fun getServer(port: Int, host: String, path: String, json: Json): ApplicationEngine =
            embeddedServer(webhookConfig.server, port = port, host = host) {
                routing {
                    post(path) {
                        val text = call.receiveText()
                        val update = json.parse(Update.serializer(), text)
                        scope.launch { updateHandlers.notify(UpdateEvent(update, bot, storage)) }
                        call.respond(HttpStatusCode.OK, "OK")
                    }
                }
            }

        suspend fun start(host: String, port: Int, path: String, wait: Boolean) {
            val exception = CompletableDeferred<Exception?>()
            val stopCallback = CompletableDeferred<Unit>()

            // send signal for server starting
            webhookControlActor.send(
                WebhookSignal.Start(host, port, path, exception, stopCallback)
            )

            // throw exception if something go wrong
            exception.await()?.let { throw it }

            // wait for the server to stop
            if (wait) stopCallback.await()
        }

        suspend fun stop(wait: Boolean) {
            val exception = CompletableDeferred<Exception?>()
            val end = CompletableDeferred<Unit>()

            // send signal for the server to stop
            webhookControlActor.send(WebhookSignal.Stop(exception, end))

            // throw exception if something go wrong
            exception.await()?.let { throw it }

            // wait for operation to end
            if (wait) end.await()
        }

        suspend fun close() {
            val closeEnd = CompletableDeferred<Unit>()
            webhookControlActor.send(WebhookSignal.Close(closeEnd))
            closeEnd.await()
        }
    }

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    private val webhook by lazy { Webhook() }


    @kotlinx.coroutines.ObsoleteCoroutinesApi
    override suspend fun stopWebhook(wait: Boolean) = webhook.stop(wait = wait)

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    override suspend fun listen(host: String, port: Int, path: String, wait: Boolean) =
        webhook.start(host = host, port = port, path = path, wait = wait)
    //</editor-fold>

    @kotlinx.coroutines.ObsoleteCoroutinesApi
    override suspend fun close() {
        coroutineScope {
            launch { polling.close() }
            launch { webhook.close() }
            launch {
                job.complete()
                job.join()
            }
        }
        bot.close()
    }
}
