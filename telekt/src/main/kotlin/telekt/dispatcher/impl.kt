package rocks.waffle.telekt.dispatcher

import com.kizitonwose.time.seconds
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.fsm.BaseStorage
import rocks.waffle.telekt.fsm.DisabledStorage
import rocks.waffle.telekt.types.Update
import rocks.waffle.telekt.types.enums.AllowedUpdate
import rocks.waffle.telekt.types.events.*


class DispatcherImpl(
    val bot: Bot,
    storage: BaseStorage? = null
) : Dispatcher {
    private val logger = KotlinLogging.logger {}
    private val storage: BaseStorage = storage ?: DisabledStorage

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


    override var isPolling: Boolean = false
        private set


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

    override suspend fun poll(relax: Float, resetWebhook: Boolean?, timeout: Int, limit: Byte?, allowedUpdates: List<AllowedUpdate>?) =
        coroutineScope {
            if (isPolling) throw RuntimeException("Polling already started")

            logger.info("Start polling")

            isPolling = true
            var offset: Int? = null

            try {
                while (isPolling) {
                    var updates: List<Update>?
                    try {
                        updates = bot.getUpdates(offset = offset, limit = limit, timeout = timeout, allowedUpdates = allowedUpdates)
                    } catch (e: java.net.SocketTimeoutException) {
                        // Telegram have no updates for us
                        continue
                    } catch (e: Exception) {
                        logger.error(e) { "Cause exception while getting updates:" }
                        delay(15.seconds.inMilliseconds.longValue)
                        continue
                    }
                    if (updates.isNotEmpty()) {
                        logger.debug("Received ${updates.size} updates.")
                        offset = updates.last().updateId + 1

                        launch { processUpdates(updates) }
                    }

                    if (relax > 0) delay(relax.seconds.inMilliseconds.longValue)
                }
            } finally {
                logger.warn("Polling is stopped.")
            }
        }

    /** Break long-polling process. */
    override suspend fun stopPolling() {
        if (isPolling) {
            logger.info("Stop polling...")
            isPolling = false
        }
    }

    override suspend fun close() {
        stopPolling()
        bot.close()
    }
}
