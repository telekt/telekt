package telekt.util

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlin.reflect.KClass

inline fun <reified T> enumByValueSerializer(noinline field: T.() -> String): KSerializer<T> where T : Enum<T> =
    EnumByValueSerializer(T::class, field)

class EnumByValueSerializer<T>(enumClass: KClass<T>, field: T.() -> String) : KSerializer<T> where T : Enum<T> {
    private val map: Map<String, T> = enumClass.enumMembers().map { it.field() to it }.toMap()
    private val name: String = enumClass.enumClassName()
    private val unmap = map.map { Pair(it.value, it.key) }.toMap()

    override val descriptor: SerialDescriptor = StringDescriptor.withName("${name}EnumDescriptor")

    override fun deserialize(decoder: Decoder): T = decoder.decodeString().let {
        map[it] ?: throw SerializationException("No enum constant $name with value \"$it\"")
    }

    override fun serialize(encoder: Encoder, obj: T) {
        encoder.encodeString(unmap[obj] ?: throw SerializationException("No value for enum constant `$obj`"))
    }
}