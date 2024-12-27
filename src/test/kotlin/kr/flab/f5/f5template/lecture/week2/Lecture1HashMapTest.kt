package kr.flab.f5.f5template.lecture.week2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class Lecture1HashMapTest {

    private val executor = Executors.newCachedThreadPool()
    private val executionCount = 100
    
    
    @Test
    fun `set,remove operation을 동시에 실행하면 최종 상태가 올바르게 반영된다`() {
        val concurrentHashMap = Lecture1HashMap<Int,Int>()
        concurrentHashMap.put(1,1)
        concurrentHashMap.put(2,2)
        concurrentHashMap.put(3,3)
        var initialSize = 3;

        val tasks = listOf(
            Runnable {
                concurrentHashMap.put(4,4)
            },
            Runnable {
                concurrentHashMap.remove(4)
            },

        )

        tasks.forEach { executor.submit(it) }
        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.SECONDS)
        assertThat(concurrentHashMap.size()).isEqualTo(initialSize)

    }

    @Test
    fun put() {
    }

    @Test
    fun remove() {
    }

    @Test
    fun putIfAbsent() {
    }

    @Test
    fun size() {
    }

    @Test
    fun isEmpty() {
    }
}