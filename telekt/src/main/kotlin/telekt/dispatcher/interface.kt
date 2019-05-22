package rocks.waffle.telekt.dispatcher

import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.fsm.BaseStorage
import rocks.waffle.telekt.fsm.DisabledStorage
import rocks.waffle.telekt.types.Update
import rocks.waffle.telekt.types.enums.AllowedUpdate
import rocks.waffle.telekt.types.events.*


@Suppress("FunctionName")
fun Dispatcher(bot: Bot, storage: BaseStorage = DisabledStorage): Dispatcher = KtorDispatcher(bot, storage)

interface Dispatcher {
    //<editor-fold desc="updates">
    /**
     * You can skip old incoming updates from queue.
     * This method is not recommended to use if you use payments or you bot has high-load.
     */
    suspend fun skipUpdates(): Unit

    /**
     * Process incoming updates.
     * This function should be used **only** for debug purposes.
     *
     */
    suspend fun processUpdates(updates: List<Update>): Unit
    //</editor-fold>

    //<editor-fold desc="polling">
    val isPolling: Boolean

    /**
     * Start long-polling
     *
     * @param relax time in seconds to relax between getUpdates requests
     * @param resetWebhook for now, just a place holder
     *
     * params [timeout], [limit] and [allowedUpdates] passes to getUpdates method.
     */
    suspend fun poll(
        relax: Float = 0.1f,
        resetWebhook: Boolean? = null,
        timeout: Int = 20,
        limit: Byte? = null,
        allowedUpdates: List<AllowedUpdate>? = null
    ): Unit

    /**
     * Start listening for webhook requests on http://{host}:{port}{path}
     *
     *  e.g.
     *  ```kotlin
     *  val bot = Bot("TOKEN")
     *  val dp = Dispatcher(bot)
     *  dp.listen(host = "localhost", port = 8080, path = "/some/path/")
     *  ```
     *  will start http server on "http://localhost:8080/some/path/"
     *
     *  @throws rocks.waffle.telekt.exceptions.WebhookWasAlreadyStarted if webhook was already started
     */
    suspend fun listen(host: String = "localhost", port: Int = 8080, path: String = "/", wait: Boolean = true)

    /**
     * Break long-polling process.
     *
     * @throws rocks.waffle.telekt.exceptions.PollingWasAlreadyStopped if polling was already stopped
     */
    suspend fun stopPolling(): Unit

    /**
     * Stop the listening for webhook requests
     *
     * @throws rocks.waffle.telekt.exceptions.WebhookWasAlreadyStopped if webhook was already stopped
     */
    suspend fun stopWebhook(wait: Boolean = true): Unit
    //</editor-fold>

    //<editor-fold desc="handler-registration">
    fun messageHandler(vararg filters: Filter<MessageEvent>, name: String? = null, block: suspend (MessageEvent) -> Unit): Unit

    fun editedMessageHandler(
        vararg filters: Filter<EditedMessageEvent>,
        name: String? = null,
        block: suspend (EditedMessageEvent) -> Unit
    ): Unit

    fun channelPostHandler(
        vararg filters: Filter<ChannelPostEvent>,
        name: String? = null,
        block: suspend (ChannelPostEvent) -> Unit
    ): Unit

    fun editedChannelPostHandler(
        vararg filters: Filter<EditedChannelPostEvent>,
        name: String? = null,
        block: suspend (EditedChannelPostEvent) -> Unit
    ): Unit

    fun inlineQueryHandler(
        vararg filters: Filter<InlineQueryEvent>,
        name: String? = null,
        block: suspend (InlineQueryEvent) -> Unit
    ): Unit

    fun chosenInlineResultHandler(
        vararg filters: Filter<ChosenInlineResultEvent>,
        name: String? = null,
        block: suspend (ChosenInlineResultEvent) -> Unit
    ): Unit

    fun callbackQueryHandler(
        vararg filters: Filter<CallbackQueryEvent>,
        name: String? = null,
        block: suspend (CallbackQueryEvent) -> Unit
    ): Unit

    fun shippingQueryHandler(
        vararg filters: Filter<ShippingQueryEvent>,
        name: String? = null,
        block: suspend (ShippingQueryEvent) -> Unit
    ): Unit

    fun preCheckoutQueryHandler(
        vararg filters: Filter<PreCheckoutQueryEvent>,
        name: String? = null,
        block: suspend (PreCheckoutQueryEvent) -> Unit
    ): Unit

    fun pollHandler(
        vararg filters: Filter<PollEvent>,
        name: String? = null,
        block: suspend (PollEvent) -> Unit
    ): Unit
    //</editor-fold>

    /**
     * Closes the dispatcher and waits for all children coroutines (e.g. handlers) to complete.
     *
     * WARNING: this method should _NOT_ be called directly from handler because it causes deadlock
     *   ([close] waits for handler to complete, and handler waits for [close] to complete)
     */
    suspend fun close(): Unit
}