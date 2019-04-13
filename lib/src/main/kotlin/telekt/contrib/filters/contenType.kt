package telekt.contrib.filters

import telekt.dispatcher.Filter
import telekt.types.Message
import telekt.types.enums.ContentType
import telekt.types.events.Event

class ContentTypeFilter<E>(contentType: ContentType, vararg contentTypes: ContentType) : Filter<E>() where E : Event<Message> {
    private val types = contentTypes.toList() + contentType

    override suspend fun test(value: E): Boolean = value.update.contentType in types
}