package kr.flab.f5.f5template.lecture.week3

import java.util.concurrent.locks.ReentrantLock
import kotlin.math.absoluteValue

/**
 * Lock만 키에 따라 분할을 하게 되면 동시성 해결이 되지 않는 일이 발생하는 것을 확인했다.
 * 키에 따라 분산을 하면 각 키에 대한 해시 인덱스를 사용하는 스레드는 동시에 접근할 수 없다.
 * 하지만 각 키에 대해 접근하지 못할 뿐이지 결국에는 HashMap에 접근하는 것이다.
 * 즉, 서로 다른 키가 동시에 hashMap에 적용할 수 있기 때문에 동시성이 발생 할 수 있다.
 * 따라서 키에 따라 각 hashmap도 분할을 해주었다.
 */
class ReetrantLockHashMap<K, V> : java.util.Map<K, V>{

    private val lockNumber = 16
    private val segmentHashMap = Array(lockNumber) { hashMapOf<K, V>() }
    private val segmentLock = Array(lockNumber) { ReentrantLock() }

    private fun getHashIndex(key: Any?): Int {
        return (key.hashCode() % lockNumber).absoluteValue
    }

    override fun get(key: Any?): V? {
        val hashIndex = getHashIndex(key)
        val lock = segmentLock[hashIndex]
        val hashMap = segmentHashMap[hashIndex]

        lock.lock()
        try {
            return hashMap[key]
        } finally {
            lock.unlock()
        }
    }

    override fun put(key: K, value: V): V? {
        val hashIndex = getHashIndex(key)
        val lock = segmentLock[hashIndex]
        val hashMap = segmentHashMap[hashIndex]

        lock.lock()
        try {
            return hashMap.put(key, value)
        } finally {
            lock.unlock()
        }
    }

    override fun remove(key: Any?): V? {
        val hashIndex = getHashIndex(key)
        val lock = segmentLock[hashIndex]
        val hashMap = segmentHashMap[hashIndex]

        lock.lock()
        try {
            return hashMap.remove(key)
        } finally {
            lock.unlock()
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        val hashIndex = getHashIndex(key)
        val lock = segmentLock[hashIndex]
        val hashMap = segmentHashMap[hashIndex]

        lock.lock()
        try {
            return hashMap.putIfAbsent(key, value)
        } finally {
            lock.unlock()
        }
    }

    override fun size(): Int {
        return (0 until lockNumber).sumOf { i ->
            val lock = segmentLock[i]
            lock.lock()
            try {
                segmentHashMap[i].size
            } finally {
                lock.unlock()
            }
        }
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