package kr.flab.f5.f5template.lecture.week2

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.AfterTest

class Lecture1HashMapTest {
    private val customConcurrentHashMap = Lecture1HashMap<Int, String>()
    private val executors = Executors.newCachedThreadPool()

    @AfterTest
    fun `hashMap 비워주기`() {
        customConcurrentHashMap.clear()
    }

    @Test
    fun `put과 get이 동시에 수행될 때도 데이터가 정확히 저장되고 읽힌다`() {
        // given
        val threadCount = 500
        val putBarrier = CyclicBarrier(threadCount)
        val getBarrier = CyclicBarrier(threadCount)
        val putLatch = CountDownLatch(threadCount)
        val getLatch = CountDownLatch(threadCount)

        val customConcurrentHashMap = Lecture1HashMap<Int, String>()
        val getResults = AtomicInteger(0)

        // when
        repeat(threadCount) { i ->
            executors.submit {
                try {
                    putBarrier.await()
                    customConcurrentHashMap.put(i, i.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    putLatch.countDown()
                }
            }
        }

        // GET 작업 스레드
        repeat(threadCount) { i ->
            executors.submit {
                try {
                    getBarrier.await()
                    val value = customConcurrentHashMap.get(i)
                    if (value.equals(i.toString())) {
                        getResults.incrementAndGet()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    getLatch.countDown()
                }
            }
        }

        putLatch.await()
        getLatch.await()

        // then
        assertThat(customConcurrentHashMap.size()).isEqualTo(threadCount)
        assertThat(getResults.get()).isEqualTo(threadCount)
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
