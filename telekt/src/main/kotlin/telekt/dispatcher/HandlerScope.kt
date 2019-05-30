package rocks.waffle.telekt.dispatcher

import rocks.waffle.telekt.bot.Bot
import rocks.waffle.telekt.fsm.FSMContext

interface TelegramEvent

class HandlerScope(val bot: Bot, val context: HandlerContext, fsmCtx: () -> FSMContext) {
    val fsmContext: FSMContext by lazy(fsmCtx)
}

class HandlerContext private constructor(private val map: Map<Key<*>, Element<*>>) {
    constructor(vararg elements: Element<*>) : this(elements.toMap())
    constructor(element: Element<*>) : this(mapOf(element.key to element))

    interface Key<out E : Element<E>>
    interface Element<out E : Element<E>> { val key: Key<E> }

    data class Entry<out E : Element<E>>(
        val key: Key<E>,
        val value: E
    )

    val keys: Set<Key<*>> get() = map.keys
    val values: Collection<Any?> get() = map.keys
    val entries: Collection<Entry<*>> get() = map.entries.map { (key, value) -> Entry(key, value) }

    /**
     * Returns the value corresponding to the given [key], or `null` if such a Key is not present in the map.
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <E : Element<E>> get(key: Key<E>): E? = map[key] as E?

    operator fun plus(other: HandlerContext): HandlerContext = HandlerContext(map + other.map)
    operator fun plus(element: Element<*>): HandlerContext = HandlerContext(map + (element.key to element))
    operator fun plus(list: List<Element<*>>): HandlerContext = HandlerContext(map + list.toMap())

    /**
     * Returns `true` if the data contains the specified [key].
     */
    operator fun contains(key: Key<*>): Boolean = key in map

    /**
     * Returns `true` if the data maps one or more keys to the specified [value].
     */
    fun containsValue(value: Element<*>): Boolean = map.containsValue(value)


    companion object {
        private fun Iterable<Element<*>>.toMap(): Map<Key<*>, Element<*>> =
            mutableMapOf<Key<*>, Element<*>>().also { map ->
                this.forEach { e -> map[e.key] = e }
            }

        private fun Array<out Element<*>>.toMap(): Map<Key<*>, Element<*>> =
            mutableMapOf<Key<*>, Element<*>>().also { map ->
                this.forEach { e -> map[e.key] = e }
            }
    }
}
