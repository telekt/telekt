package rocks.waffle.telekt.contrib.filters

import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.types.*
import rocks.waffle.telekt.types.events.Event
import rocks.waffle.telekt.types.events.TelegramEvent

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
class TextFilter<E>(private val text: String?) : Filter<E>() where E : Event<out TextableTelegramEvent> {
    override suspend fun test(value: E): Boolean = text == value.update.eventText
}
