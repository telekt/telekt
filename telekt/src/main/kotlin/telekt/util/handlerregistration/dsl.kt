package rocks.waffle.telekt.util.handlerregistration

import rocks.waffle.telekt.dispatcher.Dispatcher
import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.dispatcher.HandlerScope
import rocks.waffle.telekt.dispatcher.TelegramEvent
import rocks.waffle.telekt.types.*

@DslMarker
@Target(AnnotationTarget.CLASS)
annotation class DispatchDSLPart

@DispatchDSLPart
data class DispatchDSL(val dp: Dispatcher)

@DispatchDSLPart
data class HandlerDSL<E : TelegramEvent>(
    val dp: Dispatcher,
    val registerHandler: Dispatcher.(
        filters: Array<out Filter<E>>,
        name: String?,
        block: suspend HandlerScope.(E) -> Unit
    ) -> Unit
)

fun Dispatcher.dispatch(block: DispatchDSL.() -> Unit): Unit = DispatchDSL(this).run(block)


fun DispatchDSL.messages(block: HandlerDSL<Message>.() -> Unit): Unit = HandlerDSL(this.dp, Dispatcher::messageHandler).run(block)
fun DispatchDSL.editedMessages(block: HandlerDSL<Message>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::editedMessageHandler).run(block)

fun DispatchDSL.channelPosts(block: HandlerDSL<Message>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::channelPostHandler).run(block)

fun DispatchDSL.editedChannelPosts(block: HandlerDSL<Message>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::editedChannelPostHandler).run(block)

fun DispatchDSL.inlineQuerys(block: HandlerDSL<InlineQuery>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::inlineQueryHandler).run(block)

fun DispatchDSL.chosenInlineResults(block: HandlerDSL<ChosenInlineResult>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::chosenInlineResultHandler).run(block)

fun DispatchDSL.callbackQuerys(block: HandlerDSL<CallbackQuery>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::callbackQueryHandler).run(block)

fun DispatchDSL.shippingQuerys(block: HandlerDSL<ShippingQuery>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::shippingQueryHandler).run(block)

fun DispatchDSL.preCheckoutQuerys(block: HandlerDSL<PreCheckoutQuery>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::preCheckoutQueryHandler).run(block)

fun DispatchDSL.polls(block: HandlerDSL<Poll>.() -> Unit): Unit =
    HandlerDSL(this.dp, Dispatcher::pollHandler).run(block)


fun <T : TelegramEvent> HandlerDSL<T>.handle(vararg filters: Filter<T>, name: String? = null, block: suspend HandlerScope.(T) -> Unit) {
    dp.registerHandler(filters, name, block)
}
