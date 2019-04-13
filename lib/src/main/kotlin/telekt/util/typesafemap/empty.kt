package telekt.util.typesafemap


object EmptyTypeSafeMap : TypeSafeMap {
    override val entries: Collection<TypeSafeMap.Entry<*>> = emptySet()
    override val keys: Set<TypeSafeMap.Key<*>> = emptySet()
    override val values: Collection<Any?> = emptySet()
    override fun containsKey(key: TypeSafeMap.Key<*>): Boolean = false
    override fun containsValue(value: Any?): Boolean = false
    override fun <T> get(key: TypeSafeMap.Key<T>): T? = null
    override fun plus(other: TypeSafeMap): TypeSafeMap = other
    override fun <T> plus(other: Array<out TypeSafeMap.Entry<T>>): TypeSafeMap = typeSafeMapOf(*other)
    override fun <T> plus(other: List<TypeSafeMap.Entry<T>>): TypeSafeMap = typeSafeMapOf(other)
}
