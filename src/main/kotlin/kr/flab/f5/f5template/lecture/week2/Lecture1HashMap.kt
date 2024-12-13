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
        val currentRef = mapRef.get()
        val readNode = currentRef[hash] ?: return null
        return readNode.get().value
    }

    override fun put(key: K, value: V): V? {
        val hash = key.hashCode()
        while (true) {
            val currentMap = mapRef.get()
            val elementRef = currentMap[hash]
            val oldValue = elementRef?.get()?.value

            val newElement = Element(key, value)
            val newMap = currentMap.toMutableMap().apply {
                this[hash] = AtomicReference(newElement)
            }

            if (mapRef.compareAndSet(currentMap, newMap)) {
                return oldValue
            }
        }
    }

    override fun remove(key: Any?): V? {
        val hash = key.hashCode()
        while (true) {
            val currentMap = mapRef.get()
            val elementRef = currentMap[hash] ?: return null
            val currentElement = elementRef.get()

            if (currentElement.key != key) {
                return null // 해시 충돌로 다른 키가 저장된 경우
            }

            val newMap = currentMap.toMutableMap().apply {
                remove(hash)
            }

            if (mapRef.compareAndSet(currentMap, newMap)) {
                return currentElement.value
            }
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        val hash = key.hashCode()
        while (true) {
            val currentMap = mapRef.get()
            val elementRef = currentMap[hash]

            if (elementRef != null && elementRef.get().key == key) {
                return elementRef.get().value // 이미 존재하는 경우
            }

            val newElement = Element(key, value)
            val newMap = currentMap.toMutableMap().apply {
                if (elementRef == null) {
                    this[hash] = AtomicReference(newElement)
                }
            }

            if (mapRef.compareAndSet(currentMap, newMap)) {
                return null
            }
        }
    }

    override fun size(): Int {
        return mapRef.get().size
    }

    override fun isEmpty(): Boolean {
        return mapRef.get().isEmpty()
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
