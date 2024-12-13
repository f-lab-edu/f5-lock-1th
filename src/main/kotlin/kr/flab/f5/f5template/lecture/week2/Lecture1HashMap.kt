package kr.flab.f5.f5template.lecture.week2

import java.util.concurrent.atomic.AtomicReference

class Lecture1HashMap<K, V> : java.util.Map<K, V> {

    private val innerMap = AtomicReference(HashMap<K, V>())

    override fun get(key: Any?): V? {
        return innerMap.get()[key]
    }

    override fun put(key: K, value: V): V? {
        while (true) {
            val currentMap = innerMap.get()
            val newMap = HashMap(currentMap)
            newMap[key] = value
            if (innerMap.compareAndSet(currentMap, newMap)) {
                return currentMap[key]
            }
        }
    }

    override fun remove(key: Any?): V? {
        while (true) {
            val currentMap = innerMap.get()
            val newMap = HashMap(currentMap)
            newMap.remove(key)
            if (innerMap.compareAndSet(currentMap, newMap)) {
                return currentMap[key]
            }
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        while (true) {
            val currentMap = innerMap.get()
            val newMap = HashMap(currentMap)
            if (!newMap.containsKey(key)) {
                newMap[key] = value
                if (innerMap.compareAndSet(currentMap, newMap)) {
                    return null
                }
            } else {
                return currentMap[key]
            }
        }
    }

    override fun size(): Int {
        return innerMap.get().size
    }

    override fun isEmpty(): Boolean {
        return innerMap.get().isEmpty()
    }

    // ------- 이 아래는 구현하지 않으셔도 됩니다 ----------

    override fun containsKey(key: Any?): Boolean {
        return innerMap.get().containsKey(key)
    }

    override fun containsValue(value: Any?): Boolean {
        return innerMap.get().containsValue(value)
    }

    override fun clear() {
        innerMap.get().clear()
    }

    override fun keySet(): MutableSet<K> {
        return innerMap.get().keys
    }

    override fun values(): MutableCollection<V> {
        return innerMap.get().values
    }

    override fun entrySet(): MutableSet<MutableMap.MutableEntry<K, V>> {
        return innerMap.get().entries
    }

    override fun putAll(m: MutableMap<out K, out V>) {
        innerMap.get().putAll(m)
    }
}
