package kr.flab.f5.f5template.lecture.week2

import java.lang.invoke.MethodHandles
import java.lang.invoke.VarHandle
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

data class Node<K, V>(
    val key: K,
    @Volatile var value: V,
    val hash: Int,
    @Volatile var next: Node<K, V>? = null
)

class Lecture1HashMap<K, V> : java.util.Map<K, V> {
    private val innerMap = HashMap<K, V>()
    private val table: Array<AtomicReference<Node<K, V>>?> = Array(16) { null }

    override fun get(key: Any?): V? {
        val hash = key.hashCode()
        val index = hash and (table.size - 1)
        val currentRef = table[index] ?: return  null

        var readNode = currentRef.get()
        while(readNode != null) {
            if (readNode.key == key) {
                return readNode.value
            }
            readNode = readNode.next
        }

        return null
    }

    override fun put(key: K, value: V): V? {
        val hash = key.hashCode();
        val index = hash and (table.size - 1)

        var currentRef = table[index]
        if(currentRef == null) {
            val insertNode = Node(key, value, hash)
            currentRef = AtomicReference(insertNode)

            table[index] = currentRef
            return null
        }

        var currentNode = currentRef.get()
        while(currentNode != null) {
            if(currentNode.key == key) {
                val prevValue = currentNode.value
                currentNode.value = value
                return prevValue
            }
            currentNode = currentNode.next
        }

        val insertNode = Node(key, value, hash, currentRef.get())
        currentRef.set(insertNode)
        return null

    }

    override fun remove(key: Any?): V? {
        return innerMap.remove(key)
    }

    override fun putIfAbsent(key: K, value: V): V? {
        return innerMap.putIfAbsent(key, value)
    }

    override fun size(): Int {
        var size = 0
        for(nodeRef in table) {
            var currentNode = nodeRef?.get()
            while(currentNode != null) {
                size++
                currentNode = currentNode.next
            }
        }

        return size
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
