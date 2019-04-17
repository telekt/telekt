package rocks.waffle.telekt.contrib.filters

import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.types.CallbackQuery
import rocks.waffle.telekt.types.ChosenInlineResult
import rocks.waffle.telekt.types.InlineQuery
import rocks.waffle.telekt.types.Message
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
 * └───────────────────────┴─────────────────────────────────────┘
 */
class TextFilter<E>(private val text: String?) : Filter<E>() where E : Event<out TextableTelegramEvent> {
    override suspend fun test(value: E): Boolean = text == value.update.eventText
}
