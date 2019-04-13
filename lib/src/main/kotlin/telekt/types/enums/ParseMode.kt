package telekt.types.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import telekt.util.enumByValueSerializer

@Serializable(with = ParseMode.S::class) enum class ParseMode(val apiName: String) {
    HTML("HTML"),
    MARKDOWN("Markdown");

    @Serializer(forClass = ParseMode::class)
    object S : KSerializer<ParseMode> by enumByValueSerializer({ apiName })
}