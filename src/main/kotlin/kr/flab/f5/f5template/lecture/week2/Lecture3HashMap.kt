package kr.flab.f5.f5template.lecture.week2


/**
 * 핵심은 get 과 put 이라 생각하여 먼저 작업하고 다른것을 하려 했으나 @Syncronized 를 get,put 에 붙인것을 이겨보려고
 * 이 방법 저 방법 다 써봤지만 결국 못이기고 제출합니다. 정확히는 가끔 새로 만든것이 빠르나 거의 성능상 차이가 없어서 일단 제출합니다.
 * 확실히 동기화 처리를 전혀 안한것은 월등히 빠르기 때문에 이건 동기화를 효율적으로 못했다고 생각합니다. 어느 정도 Syncronized 를
 * 이기긴했으나... 큰 의미가 없다고 생각합니다. worst case 에서도 이겨내야 성능적으로 좋다고 생각하기 때문에 핑계를 대자면
 * 그걸 하려다가 다른것들을 못했습니다. 하지만, 이번 주차 수업에선 이게 중요하다고 생각했습니다.
 *
 * 생각의 흐름
 * static 영역에 맵을 하나 만들어둔다.
 * @Volatile 도 시도해봤지만 차이가 없었습니다. 왜인지 원인 찾는중...
 * 우선 static 영역에 두었을 땐 확실히 모든 객체가 하나의 outerMap 만 바라보는것을 확인하여 이 방법으로 진행하였습니다.
 * 결국 핵심은 같은 키에 대하여 put 이 되는 동안 get 이 먼저 이루어지는 불상사를 막고자 노력하였습니다.
 *
 * 방법은 이렇습니다 키가 들어오면 모든 객체는 object 를 상속하고 거기에 hashCode 가 있기 때문에 이걸 활용하자는 아이디어 였습니다.
 * syncronized (key.hashcode() % specificSize) 를 해주면 specificSize 의 유니크한 개수만큼만 서로를 기다리고
 * 나머지는 서로를 기다리는것 없이 진행 되기 때문에 get,put 에 Syncronized 를 한것보다 빠를것이라고 예상했으나 실제로는 그러지 않았습니다.
 *
 * 원인 분석 : 아마도 hashcode()  이 함수를 계속 타는것과 널체크가를 매번 하는것의 비용이 원인이 아닐까 합니다.
 *
 * 이건 하면서 고민한건데 결국 unique 한 값이 커지면 그만큼 싱크를 덜 탈것이고 무조건 좋을까? 에 대한 고민도 해봤지만, 초기화 하는 비용이 있어서
 * 적정값을 찾아야 할것 같습니다.
 * 하지만 만약 syncronized 가 예상과 다르게 움직인다면 이 전제도 다 무너집니다.
 *
 * 코드는 거의 작업한게 없지만 많은 고민을 해봤습니다.
 * */
class Lecture3HashMap<K, V> (
    val outMapSize : Int
): java.util.Map<K, V> {
    private val innerMap = HashMap<K, V>()

    companion object {
        val origin = object {}
        var outerMap = HashMap<Int, Any>()
    }

    override fun get(key: Any?): V? {
        val hash = key.hashCode() % outMapSize
        if(outerMap[hash] == null) {
            while(outerMap[hash] == null) {}
        }
        synchronized(outerMap[hash]!!) {
            return innerMap[key]
        }
    }

    override fun put(key: K, value: V): V? {
        val hash = key.hashCode() % outMapSize
        if(outerMap[hash] == null) {
            synchronized(origin) {
                if(outerMap[hash] == null) {
                    outerMap[hash] = true
                    //println("${hash % outMapSize} is initialized from ${Thread.currentThread()}")
                }
            }
        }
        synchronized(outerMap[hash]!!) {
            return innerMap.put(key, value)
        }
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