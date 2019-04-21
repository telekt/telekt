package rocks.waffle.telekt.dispatcher

import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.fsm.BaseStorage
import rocks.waffle.telekt.types.Update
import rocks.waffle.telekt.types.events.*
import rocks.waffle.telekt.types.Poll


@Suppress("FunctionName")
fun Dispatcher(bot: Bot, storage: BaseStorage? = null): Dispatcher = DispatcherImpl(bot, storage)

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
        allowedUpdates: List<String>? = null
    ): Unit

    /**
     * Break long-polling process.
     *
     * @throws rocks.waffle.telekt.exceptions.PollingWasAlreadyStopped if polling was already stopped
     */
    suspend fun stopPolling(): Unit
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

    suspend fun close(): Unit
}