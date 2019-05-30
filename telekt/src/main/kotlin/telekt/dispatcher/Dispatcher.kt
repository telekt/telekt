package rocks.waffle.telekt.dispatcher

import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.fsm.BaseStorage
import rocks.waffle.telekt.fsm.DisabledStorage
import rocks.waffle.telekt.types.*
import rocks.waffle.telekt.types.enums.AllowedUpdate


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
     */
    suspend fun processUpdates(updates: List<Update>): Unit
    //</editor-fold>

    //<editor-fold desc="polling">
    /**
     * Start long-polling
     *
     * @param relax time in seconds to relax between getUpdates requests
     * @param resetWebhook for now, just a place holder
     * @param wait if true [poll] will wait for polling to complete else it will return immediately
     *
     * params [timeout], [limit] and [allowedUpdates] passes to getUpdates method.
     */
    suspend fun poll(
        relax: Float = 0.1f,
        resetWebhook: Boolean? = null,
        timeout: Int = 20,
        limit: Byte? = null,
        allowedUpdates: List<AllowedUpdate>? = null,
        wait: Boolean = true
    ): Unit

    /**
     * Break long-polling process.
     *
     * @throws rocks.waffle.telekt.exceptions.PollingWasAlreadyStopped if polling was already stopped
     */
    suspend fun stopPolling(wait: Boolean = true): Unit
    //</editor-fold>

    //<editor-fold desc="webhook">
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
     * @param wait if true [listen] will wait for webhook to complete else it will return immediately
     *
     * @throws rocks.waffle.telekt.exceptions.WebhookWasAlreadyStarted if webhook was already started
     */
    suspend fun listen(host: String = "localhost", port: Int = 8080, path: String = "/", wait: Boolean = true)

    /**
     * Stop the listening for webhook requests
     *
     * @throws rocks.waffle.telekt.exceptions.WebhookWasAlreadyStopped if webhook was already stopped
     */
    suspend fun stopWebhook(wait: Boolean = true): Unit
    //</editor-fold>

    //<editor-fold desc="handler-registration">
    fun messageHandler(vararg filters: Filter<Message>, name: String? = null, block: suspend HandlerScope.(Message) -> Unit): Unit

    fun editedMessageHandler(
        vararg filters: Filter<Message>,
        name: String? = null,
        block: suspend HandlerScope.(Message) -> Unit
    ): Unit

    fun channelPostHandler(
        vararg filters: Filter<Message>,
        name: String? = null,
        block: suspend HandlerScope.(Message) -> Unit
    ): Unit

    fun editedChannelPostHandler(
        vararg filters: Filter<Message>,
        name: String? = null,
        block: suspend HandlerScope.(Message) -> Unit
    ): Unit

    fun inlineQueryHandler(
        vararg filters: Filter<InlineQuery>,
        name: String? = null,
        block: suspend HandlerScope.(InlineQuery) -> Unit
    ): Unit

    fun chosenInlineResultHandler(
        vararg filters: Filter<ChosenInlineResult>,
        name: String? = null,
        block: suspend HandlerScope.(ChosenInlineResult) -> Unit
    ): Unit

    fun callbackQueryHandler(
        vararg filters: Filter<CallbackQuery>,
        name: String? = null,
        block: suspend HandlerScope.(CallbackQuery) -> Unit
    ): Unit

    fun shippingQueryHandler(
        vararg filters: Filter<ShippingQuery>,
        name: String? = null,
        block: suspend HandlerScope.(ShippingQuery) -> Unit
    ): Unit

    fun preCheckoutQueryHandler(
        vararg filters: Filter<PreCheckoutQuery>,
        name: String? = null,
        block: suspend HandlerScope.(PreCheckoutQuery) -> Unit
    ): Unit

    fun pollHandler(
        vararg filters: Filter<Poll>,
        name: String? = null,
        block: suspend HandlerScope.(Poll) -> Unit
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