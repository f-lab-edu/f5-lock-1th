package kr.flab.f5.f5template.lecture.week2

import java.util.concurrent.atomic.AtomicInteger

/*
* map의 현재 size를 구하는 방식
*
* size를 별도로 관리할 변수를 AtomicInteger 타입으로 등록했습니다.
* put(), remove()할 때마다 증감하도록 구현하였고
* 동일한 키를 여러번 put, remove할 때를 대비하여 중간에 key에 대한 체크 로직도 추가됐습니다
* ex) if (!containsKey(key)) size.incrementAndGet()
*
* size()에서는 size 변수를 리턴하도록 구현했습니다.
*/

class AfterLectureHashMap<K, V> : java.util.Map<K, V> {

    private val lockNumber = 17
    private val locks = Array(lockNumber) { Any() }
    private var innerMap = HashMap<K, V>()
    private var size = AtomicInteger(0)

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
            if (!containsKey(key)) size.incrementAndGet()
            return innerMap.put(key, value)
        }
    }

    override fun remove(key: Any?): V? {
        val lock = getLock(key)
        synchronized(lock) {
            if (containsKey(key)) size.decrementAndGet()
            return innerMap.remove(key)
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        val lock = getLock(key)
        synchronized(lock) {
            if (!containsKey(key)) size.incrementAndGet()
            return innerMap.putIfAbsent(key, value)
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