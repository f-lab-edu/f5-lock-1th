package kr.flab.f5.f5template.lecture.week2


import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class Lecture1HashMap<K, V> : java.util.Map<K, V> {
    private val innerMap = HashMap<K, V>()
    private val atomicCounter = AtomicInteger(0)
    private val spinLock = SpinLockUtil.getInstance<K>()


    override fun get(key: Any?): V? {
        return spinLock.withLock(key) {
            innerMap[key]
        }
    }

    override fun put(key: K, value: V): V? {
        return spinLock.withLock(key) {
            val result = innerMap.put(key, value)
            if (result == null) {
                atomicCounter.incrementAndGet()
            }
            result
        }
    }

    override fun remove(key: Any?): V? {
        return spinLock.withLock(key) {
            val result = innerMap.remove(key)
            if (result != null) {
                atomicCounter.decrementAndGet()
            }
            result
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        return spinLock.withLock(key) {
            val result = innerMap.putIfAbsent(key, value)
            if (result == null) {
                atomicCounter.incrementAndGet()
            }
            result
        }
    }

    override fun size(): Int {
        return atomicCounter.get()
    }

    override fun isEmpty(): Boolean {
       return atomicCounter.get() == 0
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
