package kr.flab.f5.f5template.lecture.week4

import java.util.concurrent.atomic.AtomicLong

class StrictSizeHashMap<K,V>: java.util.Map<K, V> {

    private val bucketCount = 16
    private val buckets = Array(bucketCount) { Bucket<K,V>() }
    private val innerMap = HashMap<K,V>()
    private val sizeLock = Any()

    inner class Bucket<K, V> {
        val bucketMap = HashMap<K,V>()
        val counterCell = CounterCell(0)
    }

    inner class CounterCell(initialValue: Long) {
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
        return key.hashCode() % bucketCount
    }

    override fun get(key: Any?): V? {
        val hashIndex = getHashIndex(key)
        val bucket = buckets[hashIndex]

        synchronized(bucket) {
            return bucket.bucketMap[key]
        }
    }

    override fun put(key: K, value: V): V? {
        synchronized(sizeLock) {
            val hashIndex = getHashIndex(key)
            val bucket = buckets[hashIndex]

            val oldValue = bucket.bucketMap.put(key, value)
            if (oldValue == null) {
                bucket.counterCell.increment()
            }
            return oldValue
        }
    }

    override fun remove(key: Any?): V? {
        synchronized(sizeLock) {
            val hashIndex = getHashIndex(key)
            val bucket = buckets[hashIndex]

            val removedValue = bucket.bucketMap.remove(key)
            if (removedValue != null) {
                bucket.counterCell.decrement()
            }
            return removedValue
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        synchronized(sizeLock) {
            val hashIndex = getHashIndex(key)
            val bucket = buckets[hashIndex]

            val oldValue = bucket.bucketMap.putIfAbsent(key, value)
            if (oldValue == null) {
                bucket.counterCell.increment()
            }
            return oldValue
        }
    }

    override fun size(): Int {
        var totalSize = 0L

        synchronized(sizeLock) {
            buckets.forEach {

                totalSize += it.counterCell.get()
            }
        }

        return if (totalSize > Int.MAX_VALUE) Int.MAX_VALUE else totalSize.toInt()
    }

    override fun isEmpty(): Boolean {
        var totalSize = 0L

        synchronized(sizeLock) {
            buckets.forEach {
                totalSize += it.counterCell.get()
            }
        }

        return totalSize == 0L
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