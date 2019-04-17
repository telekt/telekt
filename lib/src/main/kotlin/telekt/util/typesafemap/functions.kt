package rocks.waffle.telekt.util.typesafemap

fun List<TypeSafeMap.Entry<Any?>>.toMap(): Map<TypeSafeMap.Key<Any?>, Any?> =
    mutableMapOf<TypeSafeMap.Key<Any?>, Any?>().also { map -> this.forEach { e -> map[e.key] = e.value } }

fun Array<out TypeSafeMap.Entry<Any?>>.toMap(): Map<TypeSafeMap.Key<Any?>, Any?> =
    mutableMapOf<TypeSafeMap.Key<Any?>, Any?>().also { map -> this.forEach { e -> map[e.key] = e.value } }


fun typeSafeMapOf(vararg elements: TypeSafeMap.Entry<Any?>): TypeSafeMap =
    if (elements.isNotEmpty()) TypeSafeMapImpl(*elements) else EmptyTypeSafeMap

fun typeSafeMapOf(): TypeSafeMap = EmptyTypeSafeMap
fun typeSafeMapOf(from: List<TypeSafeMap.Entry<Any?>>): TypeSafeMap = if (from.isNotEmpty()) TypeSafeMapImpl(from) else EmptyTypeSafeMap

private data class MapEntry<T>(override val key: TypeSafeMap.Key<T>, override val value: T) : TypeSafeMap.Entry<T>

infix fun <T> TypeSafeMap.Key<T>.with(other: T): TypeSafeMap.Entry<T> = MapEntry(this, other)
