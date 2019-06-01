package rocks.waffle.telekt.contrib.filters

import rocks.waffle.telekt.dispatcher.Filter
import rocks.waffle.telekt.dispatcher.HandlerScope
import rocks.waffle.telekt.types.Message
import rocks.waffle.telekt.types.enums.ContentType

class ContentTypeFilter(contentType: ContentType, vararg contentTypes: ContentType) : Filter<Message>() {
    private val types = contentTypes.toList() + contentType

    override suspend fun test(scope: HandlerScope, value: Message): Boolean = value.contentType in types
}