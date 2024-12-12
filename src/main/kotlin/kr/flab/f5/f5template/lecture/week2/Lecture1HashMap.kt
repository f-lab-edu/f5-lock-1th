package kr.flab.f5.f5template.lecture.week2

import java.util.concurrent.atomic.AtomicReference

data class Node<K, V>(
    val key: K,
    @Volatile var value: V,
    val next: AtomicReference<Node<K, V>?>)

class Lecture1HashMap<K, V> : java.util.Map<K, V> {
    private val innerMap = HashMap<K, V>()
    private val head = AtomicReference<Node<K, V>?>(null)

    override fun get(key: Any?): V? {
        if (key == null) return null
        var current = head.get()
        while (current != null) {
            if (current.key == key) {
                return current.value
            }
            current = current.next.get()
        }
        return null
    }

    override fun put(key: K, value: V): V? {
        while (true) {
            var prev: Node<K, V>? = null
            var current = head.get()

            while (current != null) {
                if (current.key == key) {
                    val oldValue = current.value
                    current.value = value
                    return oldValue
                }
                prev = current
                current = current.next.get()
            }

            val newNode = Node(key, value, AtomicReference(null))
            if (prev == null) {
                if (head.compareAndSet(null, newNode)) {
                    return null
                }
            } else {
                if (prev.next.compareAndSet(null, newNode)) {
                    return null
                }
            }
        }
    }

    override fun remove(key: Any?): V? {
        return innerMap.remove(key)
    }

    override fun putIfAbsent(key: K, value: V): V? {
        return innerMap.putIfAbsent(key, value)
    }

    override fun size(): Int {
        var count = 0
        var current = head.get()
        while (current != null) {
            count++
            current = current.next.get()
        }
        return count
    }

    override fun isEmpty(): Boolean {
        return head.get() == null
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
