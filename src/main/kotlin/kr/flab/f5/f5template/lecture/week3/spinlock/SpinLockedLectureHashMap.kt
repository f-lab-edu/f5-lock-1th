package kr.flab.f5.f5template.lecture.week3.spinlock

import java.util.concurrent.atomic.AtomicInteger

/**
 * SpinLocks 라이브러리 적용한 ConcurrentHashMap
 */
class SpinLockedLectureHashMap<K, V> : java.util.Map<K, V> {

    private val locks = SpinLocks()
    private var innerMap = HashMap<K, V>()
    private var size = AtomicInteger(0)

    private fun getLockIndex(key: Any?) = key.hashCode() % locks.size()

    override fun get(key: Any?): V? {
        val index = getLockIndex(key)
        locks.lock(index)
        try {
            return innerMap[key]
        } finally {
            locks.unlock(index)
        }
    }

    override fun put(key: K, value: V): V? {
        val index = getLockIndex(key)
        locks.lock(index)
        try {
            if (!containsKey(key)) size.incrementAndGet()
            return innerMap.put(key, value)
        } finally {
            locks.unlock(index)
        }
    }

    override fun remove(key: Any?): V? {
        val index = getLockIndex(key)
        locks.lock(index)
        try {
            size.decrementAndGet()
            return innerMap.remove(key)
        } finally {
            locks.unlock(index)
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        val index = getLockIndex(key)
        locks.lock(index)
        try {
            if (!containsKey(key)) size.incrementAndGet()
            return innerMap.putIfAbsent(key, value)
        } finally {
            locks.unlock(index)
        }
    }

    override fun size(): Int {
        return size.get()
    }

    override fun isEmpty(): Boolean {
        return size() == 0
    }

    // ------- 이 아래는 구현하지 않으셔도 됩니다 ----------

    override fun containsKey(key: Any?): Boolean {
        return get(key) != null
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