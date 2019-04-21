package rocks.waffle.telekt.types.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import rocks.waffle.telekt.util.enumByValueSerializer

@Serializable(with = ParseMode.S::class) enum class ParseMode(val apiName: String) {
    HTML("HTML"),
    MARKDOWN("Markdown");

    @Serializer(forClass = ParseMode::class)
    object S : KSerializer<ParseMode> by enumByValueSerializer({ apiName })
}