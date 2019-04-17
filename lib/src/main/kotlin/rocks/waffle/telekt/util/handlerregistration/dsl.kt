package rocks.waffle.telekt.util.handlerregistration

import rocks.waffle.telekt.dispatcher.Dispatcher
import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.types.events.*

@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class DispatchDSLPart

@DispatchDSLPart
data class DispatchDSL(val dp: Dispatcher)

@DispatchDSLPart
data class HandlerDSL<E : Event<*>>(
    val dp: Dispatcher,
    val registerHandler: Dispatcher.(
        filters: Array<out Filter<E>>,
        name: String?,
        block: suspend (E) -> Unit
    ) -> Unit
)

fun Dispatcher.dispatch(block: DispatchDSL.() -> Unit): Unit = DispatchDSL(this).run(block)


fun DispatchDSL.messages(block: HandlerDSL<MessageEvent>.() -> Unit): Unit = HandlerDSL(this.dp, Dispatcher::messageHandler).run(block)
fun DispatchDSL.editedMessages(block: HandlerDSL<EditedMessageEvent>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::editedMessageHandler).run(block)

fun DispatchDSL.channelPosts(block: HandlerDSL<ChannelPostEvent>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::channelPostHandler).run(block)

fun DispatchDSL.editedChannelPosts(block: HandlerDSL<EditedChannelPostEvent>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::editedChannelPostHandler).run(block)

fun DispatchDSL.inlineQuerys(block: HandlerDSL<InlineQueryEvent>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::inlineQueryHandler).run(block)

fun DispatchDSL.chosenInlineResults(block: HandlerDSL<ChosenInlineResultEvent>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::chosenInlineResultHandler).run(block)

fun DispatchDSL.callbackQuerys(block: HandlerDSL<CallbackQueryEvent>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::callbackQueryHandler).run(block)

fun DispatchDSL.shippingQuerys(block: HandlerDSL<ShippingQueryEvent>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::shippingQueryHandler).run(block)

fun DispatchDSL.preCheckoutQuerys(block: HandlerDSL<PreCheckoutQueryEvent>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::preCheckoutQueryHandler).run(block)


fun HandlerDSL<MessageEvent>.handle(vararg filters: Filter<MessageEvent>, name: String? = null, block: suspend (MessageEvent) -> Unit) {
    dp.registerHandler(filters, name, block)
}

@JvmName("editedMessageHandle")
fun HandlerDSL<EditedMessageEvent>.handle(
    vararg filters: Filter<EditedMessageEvent>,
    name: String? = null,
    block: suspend (EditedMessageEvent) -> Unit
) {
    dp.registerHandler(filters, name, block)
}

@JvmName("channelPostHandle")
fun HandlerDSL<ChannelPostEvent>.handle(
    vararg filters: Filter<ChannelPostEvent>,
    name: String? = null,
    block: suspend (ChannelPostEvent) -> Unit
) {
    dp.registerHandler(filters, name, block)
}

@JvmName("editedChannelPostHandle")
fun HandlerDSL<EditedChannelPostEvent>.handle(
    vararg filters: Filter<EditedChannelPostEvent>,
    name: String? = null,
    block: suspend (EditedChannelPostEvent) -> Unit
) {
    dp.registerHandler(filters, name, block)
}

@JvmName("inlineQueryHandle")
fun HandlerDSL<InlineQueryEvent>.handle(
    vararg filters: Filter<InlineQueryEvent>,
    name: String? = null,
    block: suspend (InlineQueryEvent) -> Unit
) {
    dp.registerHandler(filters, name, block)
}

@JvmName("chosenInlineResultHandle")
fun HandlerDSL<ChosenInlineResultEvent>.handle(
    vararg filters: Filter<ChosenInlineResultEvent>,
    name: String? = null,
    block: suspend (ChosenInlineResultEvent) -> Unit
) {
    dp.registerHandler(filters, name, block)
}

@JvmName("callbackQueryHandle")
fun HandlerDSL<CallbackQueryEvent>.handle(
    vararg filters: Filter<CallbackQueryEvent>,
    name: String? = null,
    block: suspend (CallbackQueryEvent) -> Unit
) {
    dp.registerHandler(filters, name, block)
}

@JvmName("shippingQueryHandle")
fun HandlerDSL<ShippingQueryEvent>.handle(
    vararg filters: Filter<ShippingQueryEvent>,
    name: String? = null,
    block: suspend (ShippingQueryEvent) -> Unit
) {
    dp.registerHandler(filters, name, block)
}

@JvmName("preCheckoutQueryHandle")
fun HandlerDSL<PreCheckoutQueryEvent>.handle(
    vararg filters: Filter<PreCheckoutQueryEvent>,
    name: String? = null,
    block: suspend (PreCheckoutQueryEvent) -> Unit
) {
    dp.registerHandler(filters, name, block)
}
