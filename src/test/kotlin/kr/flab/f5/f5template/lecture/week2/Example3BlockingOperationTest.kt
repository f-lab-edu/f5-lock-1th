package kr.flab.f5.f5template.lecture.week2

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlin.test.Test

@SpringBootTest
class Example3BlockingOperationTest(
    @Autowired
    private val example3BlockingOperation: Example3BlockingOperation,
) {

    private val executor = Executors.newFixedThreadPool(100)
    private val url = "https://www.hiver.co.kr/products/120533732"

    @Test
    fun `커넥션이 1개일 때 블로킹 크롤링 테스트`() {
        val start = System.currentTimeMillis()

        (1..10).map {
            example3BlockingOperation.blockingCrawling(url)
        }

        val end = System.currentTimeMillis()
        println("실행 시간: ${end - start}ms - 커넥션이 1개일 때 블로킹 크롤링 테스트")
    }

    @Test
    fun `커넥션이 1개일 때 멀티 스레딩 블로킹 크롤링 테스트`() {
        val n = 10
        var time = 0L

        (0..n).forEach {
            val start = System.currentTimeMillis()

            (1..10).map {
                executor.submit<String?> {
                    example3BlockingOperation.blockingCrawling(url)
                }
            }.forEach { it.get() }

            val end = System.currentTimeMillis()
            time += end - start
        }
        println("평균 실행 시간: ${time/n}ms - 커넥션이 1개일 때 멀티 스레딩 블로킹 크롤링 테스트")
    }

    @Test
    fun `커넥션이 1개일 때 RestTemplate 블로킹 크롤링 테스트`() {
        val n = 10
        var time = 0L

        (0..n).forEach {
            val start = System.currentTimeMillis()

            (1..10).map {
                executor.submit<String?> {
                    example3BlockingOperation.blockingCrawlingWithRestTemplate(url)
                }
            }.forEach { it.get() }

            val end = System.currentTimeMillis()
            time += end - start
        }
        println("평균 실행 시간: ${time/n}ms - 커넥션이 1개일 때 RestTemplate 블로킹 크롤링 테스트")
    }

    @Test
    fun `커넥션이 1개일 때 논블로킹 크롤링 테스트`() {
        val n = 10
        var time = 0L

        (0..n).forEach {
            val start = System.currentTimeMillis()

            val futures = (1..10).map {
                example3BlockingOperation.nonBlockingCrawling(url)
            }
            CompletableFuture.allOf(*futures.toTypedArray()).join()

            val end = System.currentTimeMillis()
            time += end - start
        }
        println("평균 실행 시간: ${time/n}ms - 커넥션이 1개일 때 논블로킹 크롤링 테스트")
    }
}
