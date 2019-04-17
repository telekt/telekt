package rocks.waffle.telekt.contrib.filters

import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.enums.ContentType
import rocks.waffle.telekt.types.events.Event

class ContentTypeFilter<E>(contentType: ContentType, vararg contentTypes: ContentType) : Filter<E>() where E : Event<Message> {
    private val types = contentTypes.toList() + contentType

    override suspend fun test(value: E): Boolean = value.update.contentType in types
}