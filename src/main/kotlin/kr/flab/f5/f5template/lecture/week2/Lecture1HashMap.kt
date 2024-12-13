package kr.flab.f5.f5template.lecture.week2

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

class Lecture1HashMap<K, V> : java.util.Map<K, V> {

    private val innerMap = HashMap<K, VersioningValue<V>>()
    private val queue = LinkedBlockingQueue<Runnable>()
    private val lock = ReentrantLock();

    override fun get(key: Any?): V? {
        return innerMap[key]?.value
    }

    override fun put(key: K, value: V): V? {
        val existing = innerMap[key]
        if(existing != null) {
            val currentVersion = existing.version.get()
            if (existing.version.compareAndSet(currentVersion, currentVersion + 1)) {
                innerMap[key] = VersioningValue(value, AtomicInteger(currentVersion + 1))
                return value;
            }
            else {
                queue.put(Runnable {
                    val currentVersion = existing.version.get()
                    // 충돌 시 버전 비교 후 업데이트 시도
                    if (existing.version.compareAndSet(currentVersion, currentVersion + 1)) {
                        innerMap[key] = VersioningValue(value, AtomicInteger(currentVersion + 1))
                    }
                })
                processQueue()
                return value;
            }
        }
        innerMap[key] = VersioningValue(value, AtomicInteger(1))
        return value;
    }

    override fun remove(key: Any?): V? {
        return innerMap.remove(key)?.value
    }

    override fun putIfAbsent(key: K, value: V): V? {
        lock.lock()
        try {
            val existing = innerMap[key]
            if (existing == null) {
                put(key, value)  // put 호출
                return null
            }
            return existing.value
        } finally {
            lock.unlock()  // 락을 해제
        }
    }

    override fun size(): Int {
        return innerMap.size
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
        return innerMap.values.map { it.value }.toMutableList()
    }

    override fun entrySet(): MutableSet<MutableMap.MutableEntry<K, V>> {
        return innerMap.entries.map {
            object : MutableMap.MutableEntry<K, V> {
                override val key: K = it.key
                override val value: V = it.value.value
                override fun setValue(newValue: V): V {
                    val oldValue = it.value.value
                    it.value.value = newValue
                    return oldValue
                }
            }
        }.toMutableSet();
    }

    override fun putAll(m: MutableMap<out K, out V>) {
        m.forEach { (key, value) ->
            innerMap[key] = VersioningValue(value, AtomicInteger(1));
        }
    }

    private fun processQueue() {
        val task = queue.poll()
        task?.run() // 큐에서 작업을 하나씩 처리
    }
}


data class VersioningValue<V>(var value: V, val version: AtomicInteger)

