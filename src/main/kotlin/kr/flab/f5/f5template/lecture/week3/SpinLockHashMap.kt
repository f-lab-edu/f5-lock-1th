package kr.flab.f5.f5template.lecture.week3

class SpinLockHashMap<K, V> : java.util.Map<K, V> {

    private val innerMap = hashMapOf<K, V>()
    private val spinLock = SpinLock()

    override fun get(key: Any?): V? {
        spinLock.lock()
        try {
            return innerMap[key]
        } finally {
            spinLock.unlock()
        }
    }

    override fun put(key: K, value: V): V? {
        spinLock.lock()
        try {
            return innerMap.put(key, value)
        } finally {
            spinLock.unlock()
        }
    }

    override fun remove(key: Any?): V? {
        spinLock.lock()
        try {
            return innerMap.remove(key)
        } finally {
            spinLock.unlock()
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        spinLock.lock()
        try {
            return innerMap.putIfAbsent(key, value)
        } finally {
            spinLock.unlock()
        }
    }

    override fun size(): Int {
        spinLock.lock()
        try {
            return innerMap.size
        } finally {
            spinLock.unlock()
        }
    }

    override fun isEmpty(): Boolean {
        spinLock.lock()
        return try {
            innerMap.isEmpty()
        } finally {
            spinLock.unlock()
        }
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