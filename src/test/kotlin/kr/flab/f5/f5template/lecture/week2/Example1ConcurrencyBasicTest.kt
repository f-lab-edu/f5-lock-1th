package kr.flab.f5.f5template.lecture.week2

import org.assertj.core.api.Assertions.assertThat
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.Test


class Example1ConcurrencyBasicTest {

    private val example1ConcurrencyBasic: Example1ConcurrencyBasic = Example1ConcurrencyBasic(0)
    // Executor의 종류와 parallelStream
    private val executors = Executors.newFixedThreadPool(1)

    @Test
    fun `동시성 테스트`() {
        val n = 10000
        val countDownLatch = CountDownLatch(n)

        repeat(n) {
            executors.submit {
                example1ConcurrencyBasic.add(1)
                countDownLatch.countDown()
            }
        }

        countDownLatch.await()

        assertThat(example1ConcurrencyBasic.get()).isEqualTo(n.toLong())
    }
}
