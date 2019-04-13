package telekt.util.typesafemap

interface TypeSafeMap {

    interface Key<out T>

    interface Entry<out T> {
        val key: Key<T>
        val value: T
    }

    val keys: Set<Key<*>>
    val values: Collection<Any?>
    val entries: Collection<Entry<*>>

    /**
     * Returns the value corresponding to the given [key], or `null` if such a Key is not present in the map.
     */
    operator fun <T> get(key: Key<T>): T?

    operator fun plus(other: TypeSafeMap): TypeSafeMap
    operator fun <T> plus(other: Array<out Entry<T>>): TypeSafeMap
    operator fun <T> plus(other: List<Entry<T>>): TypeSafeMap

    /**
     * Returns `true` if the data contains the specified [key].
     */
    fun containsKey(key: Key<*>): Boolean

    /**
     * Returns `true` if the data maps one or more keys to the specified [value].
     */
    fun containsValue(value: @UnsafeVariance Any?): Boolean

    operator fun contains(key: Key<*>): Boolean = containsKey(key)
}