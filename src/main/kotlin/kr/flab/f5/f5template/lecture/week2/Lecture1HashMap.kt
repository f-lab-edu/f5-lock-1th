package kr.flab.f5.f5template.lecture.week2

class Lecture1HashMap<K, V> : java.util.Map<K, V> {

    private val lockNumber = 100
    private val locks = Array(lockNumber) { Any() }
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
            return innerMap.put(key, value)
        }
    }

    override fun remove(key: Any?): V? {
        val lock = getLock(key)
        synchronized(lock) {
            return innerMap.remove(key)
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        val lock = getLock(key)
        synchronized(lock) {
            return innerMap.putIfAbsent(key, value)
        }
    }


    override fun size(): Int {
        var size = 0
        innerMap.keys.forEach { key ->
            val lock = getLock(key)
            synchronized(lock) {
                size++
            }
        }
        return size
    }

    override fun isEmpty(): Boolean {
        innerMap.keys.forEach { key ->
            val lock = getLock(key)
            synchronized(lock) {
                if (innerMap[key] != null) {
                    return false
                }
            }
        }
        return true
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