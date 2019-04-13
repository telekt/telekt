package telekt.util.typesafemap


internal class TypeSafeMapImpl private constructor(private var map: Map<TypeSafeMap.Key<*>, Any?>) : TypeSafeMap {
    constructor() : this(mapOf())
    constructor(from: List<TypeSafeMap.Entry<*>>) : this(from.toMap())
    constructor(vararg from: TypeSafeMap.Entry<*>) : this(from.toMap())

    data class Entry<T>(override val key: TypeSafeMap.Key<T>, override val value: T) : TypeSafeMap.Entry<T>

    override val keys: Set<TypeSafeMap.Key<*>> get() = map.keys
    override val values: Collection<Any?> get() = map.values
    override val entries: List<TypeSafeMap.Entry<*>> get() = map.entries.map { (key, value) -> Entry(key, value) }


    operator fun plus(other: TypeSafeMapImpl): TypeSafeMapImpl = TypeSafeMapImpl(map + other.map)
    override fun <T> plus(other: Array<out TypeSafeMap.Entry<T>>): TypeSafeMapImpl = TypeSafeMapImpl(map + other.toMap())
    override fun <T> plus(other: List<TypeSafeMap.Entry<T>>): TypeSafeMapImpl = TypeSafeMapImpl(map + other.toMap())
    override fun plus(other: TypeSafeMap): TypeSafeMap = this + other.entries.toList()

    /**
     * Returns the value corresponding to the given [key], or `null` if such a Key is not present in the map.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: TypeSafeMap.Key<T>): T? = map[key] as T?

    /**
     * Returns `true` if the data contains the specified [key].
     */
    override fun containsKey(key: TypeSafeMap.Key<*>): Boolean = map.containsKey(key)

    /**
     * Returns `true` if the data maps one or more keys to the specified [value].
     */
    override fun containsValue(value: @UnsafeVariance Any?): Boolean = map.containsValue(value)

    override operator fun contains(key: TypeSafeMap.Key<*>): Boolean = containsKey(key)

    override fun equals(other: Any?): Boolean = if (other is TypeSafeMapImpl) map == other.map else false
    override fun hashCode(): Int = map.hashCode()
    override fun toString(): String = map.toString()
}
