package kr.flab.f5.f5template.lecture.week2

import java.util.concurrent.atomic.AtomicReference

data class Element<K, V>(
    val key: K,
    var value: V,
)

class Lecture1HashMap<K, V> : java.util.Map<K, V> {
    private val innerMap = HashMap<K, V>()
    private val mapRef: AtomicReference<Map<Int, AtomicReference<Element<K, V>>>> = AtomicReference(emptyMap())

    override fun get(key: Any?): V? {
        val hash = key.hashCode()
        val currentRef = mapRef.get() ?: return  null

        val readNode = currentRef[hash]
        if (readNode != null) return readNode.get().value
        return null
    }

    override fun put(key: K, value: V): V? {
        val hash = key.hashCode();
        var currentMap: Map<Int, AtomicReference<Element<K, V>>>
        var newMap: Map<Int, AtomicReference<Element<K, V>>>

        do {
            currentMap = mapRef.get()

            newMap = if (currentMap.isEmpty()) {
                val newElement = Element(key, value)
                mapOf(hash to AtomicReference(newElement))
            } else {
                currentMap.toMutableMap().apply {
                    if (containsKey(hash)) {
                        val elementRef = this[hash]
                        val currentElement = elementRef?.get()
                        elementRef?.set(currentElement?.copy(value = value) ?: Element(key, value))
                    } else {
                        val newElement = Element(key, value)
                        this[hash] = AtomicReference(newElement)
                    }
                }
            }
        } while(!mapRef.compareAndSet(currentMap, newMap))
        return null
    }

    override fun remove(key: Any?): V? {
        return innerMap.remove(key)
    }

    override fun putIfAbsent(key: K, value: V): V? {
        return innerMap.putIfAbsent(key, value)
    }

    override fun size(): Int {
        return mapRef.get().size - 1
    }

    override fun isEmpty(): Boolean {
        return innerMap.isEmpty()
    }

    // ------- 이 아래는 구현하지 않으셔도 됩니다 ----------

    override fun containsKey(key: Any?): Boolean {
        return innerMap.containsKey(key)
    }

    override fun containsValue(value: Any?): Boolean {
        return innerMap.containsValue(value)
    }

    override fun clear() {
        innerMap.clear()
    }

    override fun keySet(): MutableSet<K> {
        return innerMap.keys
    }

    override fun values(): MutableCollection<V> {
        return innerMap.values
    }

    override fun entrySet(): MutableSet<MutableMap.MutableEntry<K, V>> {
        return innerMap.entries
    }

    override fun putAll(m: MutableMap<out K, out V>) {
        innerMap.putAll(m)
    }
}
