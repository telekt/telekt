package telekt.util

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor


@Serializer(forClass = Unit::class)
object UnitSerializer : KSerializer<Unit> {
    override val descriptor: SerialDescriptor = StringDescriptor.withName("UnitDescriptor")
    override fun serialize(encoder: Encoder, obj: Unit) {
        encoder.encodeUnit()
    }

    override fun deserialize(decoder: Decoder) {
        val t = decoder.decodeBoolean()
        if (!t) SerializationException("This method must return `true`, but it have return `false`, may be there is (are) error(s) in lib. Please open issie on github, or connect to developer.")
    }
}

fun Unit.serializer(): KSerializer<Unit> = UnitSerializer