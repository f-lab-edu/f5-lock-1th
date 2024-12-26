package kr.flab.f5.f5template.lecture.week4;
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.atomic.AtomicInteger

class Lecture1HashMap<K, V> : java.util.Map<K, V> {
    private val innerMap = HashMap<K, V>()
    private val atomicCounter = AtomicInteger(0)
    private val spinLock = SpinLockUtil.getInstance<K>()
    private val queue: BlockingQueue<Unit> = ArrayBlockingQueue(1)

    init {
        queue.put(Unit) // 큐에 초기값 넣어 두어 첫 번째 스레드가 실행될 수 있도록 함
    }

    override fun get(key: Any?): V? {
        checkBlocking()
        return spinLock.withLock(key) {
            innerMap[key]
        }
    }

    override fun put(key: K, value: V): V? {
        checkBlocking()
        return spinLock.withLock(key) {
            val result = innerMap.put(key, value)
            if (result == null) {
             atomicCounter.incrementAndGet()
            }
            result
        }
    }

    override fun remove(key: Any?): V? {
        checkBlocking()
        return spinLock.withLock(key) {
        val result = innerMap.remove(key)
            if (result != null) {
                atomicCounter.decrementAndGet()
            }
            result
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        checkBlocking()
        return spinLock.withLock(key) {
            val result = innerMap.putIfAbsent(key, value)
            if (result == null) {
                atomicCounter.incrementAndGet()
            }
            result
        }
    }

    override fun size(): Int {
        try {
            queue.take()
            val result = atomicCounter.get()
            return result
        } finally {
            queue.put(Unit)
        }
    }

    private fun checkBlocking() {
        if (queue.remainingCapacity() == 0) {
            queue.take() // size()가 실행 중이면 대기하도록 함
        }
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