package kr.flab.f5.f5template.lecture.week2

import java.util.concurrent.locks.ReentrantReadWriteLock

/*
    HashMap으로부터 ConcurrentHashMap 만들어보기

    생각 플로우
    DB랑 비슷하다고 생각해보자. 읽을 때는 SharedLock, 수정할 때는 Exclusive Lock을 사용하면 되지 않을까?
    SharedLock은 중첩이 가능하기 때문에 여러 스레드들이 HashMap에 접근할 수 있도록하고, SharedLock이 걸려있는 상태에서는 ExclusiveLock을 얻을 수 없게 하자.
    보통 Map과 같은 객체는 get 작업이 put 작업보다 훨씬 많이 일어날 것이다.

    이렇게 구현하려면 Lock 순서를 조정할 수 있어야 하기 때문에, ReentrantLock을 사용해야한다.(tryLock을 통해 특정 Lock을 획득할 수 있는지 체크해야한다.)

    SharedLock 대상 메소드는 get, size, isEmpty 총 3개로 Map 내의 데이터에 수정을 가하지 않는 메소드들이다.
    ExclusiveLock 대상 메소드는 put, remove, putIfAbsent 총 3가지이다.
    그리고 putIfAbsent의 경우, key값이 이미 Map 내에 있으면 put할 필요가 없다. 따라서, 해당 값이 존재하는지 체크하기위해서 SharedLock만 처음에 걸어도 될 듯 하다.
    어차피 SharedLock을 건 상태라면, 수정이 일어나고 있지 않은 상태기 때문에 정합성은 보장할 수 있다.

    다만 예상되는 문제점은 정상적인 put 요청이 너무 나중에 걸리게되면, 읽어올 수 있을 값들을 읽어오지 못할 경우가 생길 것 같다. 다만 그 확률은 희박하므로 성능을 택한다.

    또한, 오래 기다린 스레드가 먼저 Lock을 획득할 수 있도록 fairness 값은 true로 한다.


    SharedLock은 어떻게 구현할 수 있을까? ReentrantLock도 결국 Lock 객체이기 때문에, 중첩락을 구현할 수 없다.
    ++ 검색을 통해 ReentrantReadWriteLock 객체가 있는 것을 발견.
 */
class Lecture1HashMap<K, V> : java.util.Map<K, V> {

    private val innerMap = HashMap<K, V>()
    private val rwLock = ReentrantReadWriteLock(true)
    private val sharedLock = rwLock.readLock()
    private val exclusiveLock = rwLock.writeLock()

    override fun get(key: Any?): V? {
        sharedLock.lock()
        val value = innerMap[key]
        sharedLock.unlock()
        return value
    }

    /*
        put, remove, putIfAbsent은 수정을 가하는 Lock이다. 작업을 수행할 때 꼭 적용한다.
    */
    override fun put(key: K, value: V): V? {
        exclusiveLock.lock()
        val result = innerMap.put(key, value)
        exclusiveLock.unlock()
        return result
    }

    override fun remove(key: Any?): V? {
        exclusiveLock.lock()
        val result = innerMap.remove(key)
        exclusiveLock.unlock()
        return result
    }

    override fun putIfAbsent(key: K, value: V): V? {
        sharedLock.lock()
        if (innerMap.containsKey(key)) {
            val result = innerMap[key]
            sharedLock.unlock()
            return result
        } else {
            sharedLock.unlock()
            exclusiveLock.lock()
            val result = innerMap.put(key, value)
            exclusiveLock.unlock()
            return result
        }
    }

    override fun size(): Int {
        sharedLock.lock()
        val result = innerMap.size
        sharedLock.unlock()
        return result
    }

    override fun isEmpty(): Boolean {
        sharedLock.lock()
        val result = innerMap.isEmpty()
        sharedLock.unlock()
        return result
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
