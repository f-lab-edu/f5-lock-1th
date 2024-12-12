package kr.flab.f5.f5template.lecture.week2

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.Test

class Example4SingletonTest {

    private val executor = Executors.newFixedThreadPool(100)

    @Test
    fun `싱글톤은 1개의 객체만 만들어져야 한다`() {
        val n = 1000
        val latch = CountDownLatch(1)
        val futures = (1..n).map {
            executor.submit<Example4Singleton> {
                latch.await()
                Example4Singleton.getInstance()
            }
        }

        latch.countDown()

        val objects = futures.map {
            it.get()
        }.distinct()

        assertThat(objects.size).isEqualTo(1)
    }
}
