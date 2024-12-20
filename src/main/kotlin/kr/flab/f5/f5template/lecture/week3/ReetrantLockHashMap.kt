package kr.flab.f5.f5template.lecture.week3

import java.util.concurrent.locks.ReentrantLock
import kotlin.math.absoluteValue

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