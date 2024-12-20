package kr.flab.f5.f5template.lecture.week2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class AfterLectureHashMapTest {

    private val executors = Executors.newFixedThreadPool(100)
    private val executionCount = 300

    @DisplayName("멀티스레드 환경에서 put할 때, thread safe를 검증한다")
    @RepeatedTest(50)
    fun threadSafe() {
        val concurrencyMap = AfterLectureHashMap<Int, String>()
        val countDownLatch = CountDownLatch(executionCount);

        repeat(executionCount) {
            executors.submit {
                concurrencyMap.put(it, "값 ${it}")
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
        executors.shutdown()

        assertThat(concurrencyMap.size()).isEqualTo(executionCount)
    }


    @DisplayName("동일한 key로 N번 put하면 size는 1이다")
    @RepeatedTest(50)
    fun putSameValue() {
        val concurrencyMap = AfterLectureHashMap<String, String>()
        val countDownLatch = CountDownLatch(executionCount);
        val key = "same key"

        repeat(executionCount) {
            executors.submit {
                concurrencyMap.put(key, "값 ${it}")
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
        executors.shutdown()

        assertThat(concurrencyMap.size()).isEqualTo(1)
    }

}