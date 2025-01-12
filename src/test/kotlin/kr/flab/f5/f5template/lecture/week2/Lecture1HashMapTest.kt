package kr.flab.f5.f5template.lecture.week2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.RepeatedTest
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class Lecture1HashMapTest {
    private val executor = Executors.newFixedThreadPool(100)

    @Test
    fun `PUT 연산`() {
        val map = Lecture1HashMap<Int, Int>()
        val threadCount = 100
        val latch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            executor.execute {
                map.put(i, i * 10)
                latch.countDown()
            }
        }

        latch.await(5, TimeUnit.SECONDS)
        executor.shutdown()

        assertEquals(threadCount, map.size())
        for (i in 0 until threadCount) {
            assertEquals(i * 10, map.get(i))
        }
    }

    @Test
    fun `동일 KEY PUT 연산`() {
        val map = Lecture1HashMap<String, Int>()
        val threadCount = 100
        val key = "KEY"
        val latch = CountDownLatch(threadCount)

        for(i in 0 until threadCount) {
            executor.execute{
                map.put(key, i)
                latch.countDown()
            }
        }

        latch.await(5, TimeUnit.SECONDS)
        executor.shutdown()

        assertEquals(1, map.size())
        val result = map.get(key)
        assertNotNull(result)
    }

    @Test
    fun `GET 연산`() {
        val map = Lecture1HashMap<Int, Int>()
        val threadCount = 50
        val latch = CountDownLatch(threadCount * 2)

        for(i in 0 until threadCount) {
            map.put(i, i)
        }

        for(i in 0 until threadCount) {
            executor.execute{
                map.put(i, i * 10)
                latch.countDown()
            }
            executor.execute{
                val value = map.get(i)
                assertTrue(value == i || value == i * 10)
                latch.countDown()
            }
        }

        latch.await(10, TimeUnit.SECONDS)
        executor.shutdown()

        for(i in 0 until threadCount) {
            assertEquals(i * 10, map.get(i))
        }
    }

    @Test
    fun `REMOVE 연산`() {
        val map = Lecture1HashMap<Int, Int>()
        val threadCount = 50
        val latch = CountDownLatch(threadCount * 2)

        for(i in 0 until threadCount) {
            executor.execute{
                map.put(i, i * 10)
                latch.countDown()
            }
            executor.execute{
                map.remove(i)
                latch.countDown()
            }
        }

        assertEquals(0, map.size())
    }

    @Test
    fun `PUT IF ABSENT 연산`() {
        val map = Lecture1HashMap<String, Int>()
        val threadCount = 100
        val key = "key"
        val latch = CountDownLatch(threadCount)
        val results = mutableListOf<Int?>()

        for (i in 0 until threadCount) {
            executor.execute {
                val result = map.putIfAbsent(key, i)
                synchronized(results) {
                    results.add(result)
                }
                latch.countDown()
            }
        }

        latch.await(5, TimeUnit.SECONDS)
        executor.shutdown()

        val successfulPuts = results.filter { it == null }
        assertEquals(1, successfulPuts.size)

        val finalValue = map.get(key)
        assertNotNull(finalValue)
        assertTrue(finalValue in 0 until threadCount)
        assertEquals(1, map.size())
    }
}