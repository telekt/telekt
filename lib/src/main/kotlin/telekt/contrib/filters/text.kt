package telekt.contrib.filters

import telekt.dispatcher.Filter
import telekt.types.CallbackQuery
import telekt.types.ChosenInlineResult
import telekt.types.InlineQuery
import telekt.types.Message
import telekt.types.events.Event
import telekt.types.events.TelegramEvent

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
