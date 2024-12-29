package kr.flab.f5.f5template.lecture.week3

class LectureHashMapWithSpinLock<K, V> : java.util.Map<K, V> {

    private val innerMap = HashMap<K, V>()

    override fun get(key: Any?): V? {
        return innerMap[key]
    }

    override fun put(key: K, value: V): V? {
        return innerMap.put(key, value)
    }

    override fun remove(key: Any?): V? {
        return innerMap.remove(key)
    }

    override fun putIfAbsent(key: K, value: V): V? {
        return innerMap.putIfAbsent(key, value)
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
        return innerMap.values
    }

    override fun entrySet(): MutableSet<MutableMap.MutableEntry<K, V>> {
        return innerMap.entries
    }

    override fun putAll(m: MutableMap<out K, out V>) {
        innerMap.putAll(m)
    }
}
