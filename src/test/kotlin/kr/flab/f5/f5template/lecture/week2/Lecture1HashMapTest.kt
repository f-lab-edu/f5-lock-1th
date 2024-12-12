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

class CASList<T> {

    // AtomicReference로 List를 감싸서 CAS 연산을 구현
    private val listReference = AtomicReference<List<T>>(emptyList())

    // 원자적으로 값 추가
    fun add(item: T): Boolean {
        var currentList: List<T>
        var newList: List<T>

        do {
            currentList = listReference.get()
            newList = currentList + item // 새로운 아이템을 추가한 새로운 리스트 생성
        } while (!listReference.compareAndSet(currentList, newList)) // CAS 연산

        return true
    }

    // 현재 리스트 반환
    fun getList(): List<T> {
        return listReference.get()
    }

    fun size(): Int {
        return listReference.get().size
    }
}

class Lecture1HashMapTest {

    private val executor = Executors.newFixedThreadPool(100)

    @RepeatedTest(10)
    fun test() {
        val casList = CASList<Int>()

        // 스레드 100개 생성하여 리스트에 add 수행
        val threads = mutableListOf<Thread>()
        for (i in 1..100) {
            threads.add(thread {
                casList.add(i)
                println("Thread $i: added $i")
            })
        }

        // 모든 스레드가 종료될 때까지 기다림
        threads.forEach { it.join() }

        // 최종 결과 출력
        println("Final List: ${casList.size()}")
        assertEquals(100, casList.size(), "FAIL")
    }

    @RepeatedTest(10)
    fun `PUT 연산`() {
        val threadNum = 100
        val incrementsPerThread = 1000
        val map = Lecture1HashMap<Int, Int>()
        val latch = CountDownLatch(1) // 모든 스레드가 동시에 시작하도록 제어
        val futures: List<Future<Unit>> = (1..threadNum).map {
            threadId -> executor.submit<Unit> {
                latch.await()
                for(i in 0 .. incrementsPerThread) {
                    val key = threadId * incrementsPerThread + i
                    map.put(key, key)
                }
            }
        }

        val startTime = System.nanoTime()
        latch.countDown()

        futures.forEach { future ->
            try {
                future.get(60, TimeUnit.SECONDS)
            } catch (e: Exception) {
                fail("스레드 작업이 시간 내에 완료되지 않았습니다: ${e.message}")
            }
        }

        val endTime = System.nanoTime()
        val durationMs = (endTime - startTime) / 1_000_000

        println("Final List: ${map.size()}")
        assertEquals(100, map.size(), "FAIL")
    }

    @RepeatedTest(10)
    fun `동시 PUT 연산`() {
        val threadNum = 10
        val incrementsPerThread = 100
        val map = Lecture1HashMap<Int, Int>()
        val latch = CountDownLatch(1) // 모든 스레드가 동시에 시작하도록 제어
        val futures: List<Future<Unit>> = (1..threadNum).map {
            threadId -> executor.submit<Unit> {
                latch.await()
                for(i in 0 .. incrementsPerThread) {
                    val key = threadId * incrementsPerThread + i
                    map.put(key, i);
                }
            }
        }
        val startTime = System.nanoTime()
        latch.countDown()

        futures.forEach { future ->
            try {
                future.get(60, TimeUnit.SECONDS)
            } catch (e: Exception) {
                fail("스레드 작업이 시간 내에 완료되지 않았습니다: ${e.message}")
            }
        }

        val endTime = System.nanoTime()
        val durationMs = (endTime - startTime) / 1_000_000

        val result = map.size()
        val expectedValue = threadNum * incrementsPerThread
        assertEquals(expectedValue, result, "예상되는 값과 다릅니다.")

        println("최종 값 : $result, 예상 값 : $expectedValue")
        println("테스트 소요 시간: $durationMs ms")
    }
}