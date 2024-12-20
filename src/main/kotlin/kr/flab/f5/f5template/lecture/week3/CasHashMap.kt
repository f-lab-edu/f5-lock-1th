package kr.flab.f5.f5template.lecture.week3

import java.util.concurrent.atomic.AtomicLong
import kotlin.math.absoluteValue

/**
 * 동시성을 보장시키기위해 CAS 연산과 synchronized를 함께 사용하도록 구현했다.
 * 락을 이용해 값을 넣거나 삭제할 때 size의 값도 동시에 변경될 수 있도록 했다.
 * 따라서 size를 구할 때 각 분산된 hashmap의 크기를 각각 더해줘서 동시성이 보장되면서 크기를 구할 수 있도록 했다.
 */
class CasHashMap<K, V> : java.util.Map<K, V> {

    private val lockNumber = 16
    private val segmentHashMap = Array(lockNumber) { hashMapOf<K, V>() }
    private val locks = Array(lockNumber) { Any() }
    private val counterCell = Array(lockNumber) { CounterCell(0) }

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
        return (key.hashCode() % lockNumber).absoluteValue
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
        return false
    }

    // ------- 이 아래는 구현하지 않으셔도 됩니다 ----------

    override fun containsKey(key: Any?): Boolean {
        return false
    }

    override fun containsValue(value: Any?): Boolean {
        return false
    }

    override fun clear() {
    }

    override fun keySet(): MutableSet<K> {
        return segmentHashMap[0].keys
    }

    override fun values(): MutableCollection<V> {
        return segmentHashMap[0].values
    }

    override fun entrySet(): MutableSet<MutableMap.MutableEntry<K, V>> {
        return segmentHashMap[0].entries
    }

    override fun putAll(m: MutableMap<out K, out V>) {
        segmentHashMap[0].putAll(m)
    }
}