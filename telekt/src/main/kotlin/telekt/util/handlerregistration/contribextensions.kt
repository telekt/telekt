package rocks.waffle.telekt.util.handlerregistration

import rocks.waffle.telekt.contrib.filters.*
import rocks.waffle.telekt.dispatcher.TelegramEvent
import rocks.waffle.telekt.fsm.State
import rocks.waffle.telekt.types.*
import rocks.waffle.telekt.types.enums.ContentType


fun @Suppress("unused") HandlerDSL<Message>.command(
    command: String,
    vararg commands: String,
    prefixes: Array<Char> = arrayOf('/'),
    ignoreCase: Boolean = false,
    ignoreMention: Boolean = false
): CommandFilter = CommandFilter(
    command,
    *commands,
    prefixes = prefixes,
    ignoreCase = ignoreCase,
    ignoreMention = ignoreMention
)


fun <T : TelegramEvent> @Suppress("unused") HandlerDSL<T>.state(state: State?): StateFilter<T> = StateFilter(state)


fun @Suppress("unused") HandlerDSL<Message>.contentTypes(
    contentType: ContentType,
    vararg contentTypes: ContentType
): ContentTypeFilter = ContentTypeFilter(contentType, *contentTypes)


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
 * ├───────────────────────┼─────────────────────────────────────┤
 * │        [Poll]         │ [Poll.question]                     │
 * └───────────────────────┴─────────────────────────────────────┘
 */
fun <T : TextableTelegramEvent> @Suppress("unused") HandlerDSL<T>.text(text: String?): TextFilter<T> = TextFilter(text)
