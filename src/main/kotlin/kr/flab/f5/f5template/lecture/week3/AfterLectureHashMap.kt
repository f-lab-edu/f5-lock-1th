package kr.flab.f5.f5template.lecture.week2

/*
* size() 호출 시점의 map size를 구하는 방식
*
* 모든 데이터를 읽을 때 사용할 락 allReadLock을 추가하였습니다.
* size() 에서는 allReadLock를 이용하여 map의 사이즈를 구했습니다.
*
* put(), remove()에서는 이중 synchronized 블럭을 사용하여
* allReadLock가 점유 중일 때, 즉 size를 구하는 동안은 데이터를 put, remove 하지 못하게 막았습니다.
* */
class AfterLectureHashMap<K, V> : java.util.Map<K, V> {

    private val lockNumber = 17
    private val locks = Array(lockNumber) { Any() }
    private val allReadLock = Any()
    private var innerMap = HashMap<K, V>()

    private fun getLock(key: Any?): Any {
        val index = key.hashCode() % lockNumber
        return locks[index]
    }

    override fun get(key: Any?): V? {
        val lock = getLock(key)
        synchronized(lock) {
            return innerMap[key]
        }
    }

    override fun put(key: K, value: V): V? {
        val lock = getLock(key)
        synchronized(lock) {
            synchronized(allReadLock) {
                return innerMap.put(key, value)
            }
        }
    }

    override fun remove(key: Any?): V? {
        val lock = getLock(key)
        synchronized(lock) {
            synchronized(allReadLock) {
                return innerMap.remove(key)
            }
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        val lock = getLock(key)
        synchronized(lock) {
            synchronized(allReadLock) {
                return innerMap.putIfAbsent(key, value)
            }
        }
    }

    override fun size(): Int {
        synchronized(allReadLock) {
            return innerMap.size
        }
    }

    override fun isEmpty(): Boolean {
        return size() == 0
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