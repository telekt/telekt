package rocks.waffle.telekt.util.handlerregistration

import rocks.waffle.telekt.contrib.filters.*
import rocks.waffle.telekt.fsm.State
import rocks.waffle.telekt.types.CallbackQuery
import rocks.waffle.telekt.types.ChosenInlineResult
import rocks.waffle.telekt.types.InlineQuery
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.enums.ContentType
import rocks.waffle.telekt.types.events.Event


fun <E> @Suppress("unused") HandlerDSL<E>.command(
    command: String,
    vararg commands: String,
    prefixes: Array<Char> = arrayOf('/'),
    ignoreCase: Boolean = false,
    ignoreMention: Boolean = false
): CommandFilter<E> where E : Event<Message> = CommandFilter(
    command,
    *commands,
    prefixes = prefixes,
    ignoreCase = ignoreCase,
    ignoreMention = ignoreMention
)


fun <E> @Suppress("unused") HandlerDSL<E>.state(state: State?): StateFilter<E> where E : Event<*> =
    StateFilter(state)


fun <E> @Suppress("unused") HandlerDSL<E>.contentTypes(
    contentType: ContentType,
    vararg contentTypes: ContentType
): ContentTypeFilter<E> where E : Event<Message> =
    ContentTypeFilter(contentType, *contentTypes)


/**
 * Text filter.
 *
 * Successes if text of event is equals to [text].
 *
 * ┌───────────────────────┬─────────────────────────────────────┐
 * │      event type       │             'text' field            │
 * ├───────────────────────┼─────────────────────────────────────┤
 * │       [Message]       │ [Message.text] ?: [Message.caption] │
 * ├───────────────────────┼─────────────────────────────────────┤
 * │     [InlineQuery]     │ [InlineQuery.query]                 │
 * ├───────────────────────┼─────────────────────────────────────┤
 * │    [CallbackQuery]    │ [CallbackQuery.data]                │
 * ├───────────────────────┼─────────────────────────────────────┤
 * │ [ChosenInlineResult]  │ [ChosenInlineResult.query]          │
 * └───────────────────────┴─────────────────────────────────────┘
 */
fun <E> @Suppress("unused") HandlerDSL<E>.text(text: String?): TextFilter<E> where E : Event<out TextableTelegramEvent> =
    TextFilter(text)
