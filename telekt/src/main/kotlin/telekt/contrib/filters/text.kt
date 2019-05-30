package rocks.waffle.telekt.contrib.filters

import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.dispatcher.HandlerScope
import rocks.waffle.telekt.dispatcher.TelegramEvent
import rocks.waffle.telekt.types.*

interface TextableTelegramEvent : TelegramEvent {
    val eventText: String?
}

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
class TextFilter<T : TextableTelegramEvent>(private val text: String?) : Filter<T>() {
    override suspend fun test(scope: HandlerScope, value: T): Boolean = text == value.eventText
}
