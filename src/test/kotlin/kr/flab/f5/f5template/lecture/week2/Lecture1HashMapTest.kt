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

class Lecture1HashMapTest {

    private val executor = Executors.newFixedThreadPool(100)

    @RepeatedTest(10)
    fun `동시 PUT 연산`() {
        val threadNum = 100
        val incrementsPerThread = 1000
        val map = Lecture1HashMap<Int, Int>()
        val latch = CountDownLatch(1) // 모든 스레드가 동시에 시작하도록 제어
        val futures: List<Future<Unit>> = (1..threadNum).map {
            threadId -> executor.submit<Unit> {
                latch.await()
                for(i in 1 .. incrementsPerThread) {
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