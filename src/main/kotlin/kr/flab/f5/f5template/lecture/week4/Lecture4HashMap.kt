package kr.flab.f5.f5template.lecture.week4

import java.util.concurrent.atomic.AtomicLong

class Lecture4HashMap<K,V>: java.util.Map<K, V> {

    private val lockNumber = 16
    private val segmentHashMap = Array(lockNumber) { hashMapOf<K, V>() }
    private val locks = Array(lockNumber) { Any() }
    private val counterCell = Array(lockNumber) { CounterCell(0) }
    private val innerMap = HashMap<K,V>()

    private class CounterCell(initialValue: Long) {
        private val value = AtomicLong(initialValue)

        fun increment() {
            value.incrementAndGet()
        }

        fun decrement() {
            value.decrementAndGet()
        }

        fun get(): Long {
            return value.get()
        }
    }

    private fun getHashIndex(key: Any?): Int {
        return (key.hashCode() % lockNumber)
    }

    override fun get(key: Any?): V? {
        val hashIndex = getHashIndex(key)
        val hashMap = segmentHashMap[hashIndex]

        synchronized(locks[hashIndex]) {
            return hashMap[key]
        }
    }

    override fun put(key: K, value: V): V? {
        val hashIndex = getHashIndex(key)
        val hashMap = segmentHashMap[hashIndex]
        val counter = counterCell[hashIndex]

        synchronized(locks[hashIndex]) {
            val oldValue = hashMap.put(key, value)
            if (oldValue == null) {
                counter.increment()
            }
            return oldValue
        }
    }

    override fun remove(key: Any?): V? {
        val hashIndex = getHashIndex(key)
        val hashMap = segmentHashMap[hashIndex]
        val counter = counterCell[hashIndex]

        synchronized(locks[hashIndex]) {
            val removedValue = hashMap.remove(key)
            if (removedValue != null) {
                counter.decrement()
            }
            return removedValue
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        val hashIndex = getHashIndex(key)
        val hashMap = segmentHashMap[hashIndex]
        val counter = counterCell[hashIndex]

        synchronized(locks[hashIndex]) {
            val oldValue = hashMap.putIfAbsent(key, value)
            if (oldValue == null) {
                counter.increment()
            }
            return oldValue
        }
    }

    override fun size(): Int {
        val totalSize = counterCell.sumOf { it.get() }
        return if (totalSize > Int.MAX_VALUE) Int.MAX_VALUE else totalSize.toInt()
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