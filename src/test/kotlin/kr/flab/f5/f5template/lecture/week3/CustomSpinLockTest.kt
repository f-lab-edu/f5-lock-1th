package kr.flab.f5.f5template.lecture.week3

import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.CountDownLatch
import kotlin.test.Test

class CustomSpinLockTest {
    @Test
    fun `동시에 counter 증가 테스트`() {
        val spinLock = CustomSpinLock()
        var counter = 0

        val threadCount = 10
        val incrementsPerThread = 100_000

        // 스레드가 동시에 시작하도록 CountDownLatch 사용
        val startLatch = CountDownLatch(1)
        // 모든 스레드가 끝날 때까지 기다리는 Latch
        val doneLatch = CountDownLatch(threadCount)

        repeat(threadCount) {
            Thread {
                // 스레드 준비 완료 → startLatch가 0 될 때까지 대기
                startLatch.await()

                // 실제 작업
                repeat(incrementsPerThread) {
                    spinLock.withLock {
                        counter++
                    }
                }

                doneLatch.countDown()
            }.start()
        }

        // 모든 스레드 동시에 시작
        startLatch.countDown()

        // 모든 스레드가 끝날 때까지 대기
        doneLatch.await()

        val expected = threadCount * incrementsPerThread
        assertEquals(expected, counter, "SpinLock이 정상 동작하면 counter == $expected 여야 함")
    }
}