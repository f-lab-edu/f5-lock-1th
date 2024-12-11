package kr.flab.f5.f5template.lecture.week1

import com.google.common.util.concurrent.RateLimiter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlin.test.Test

@SpringBootTest
class CrawlingExampleTest(
    @Autowired
    private val crawlingExample: CrawlingExample,
) {

    private val executor = Executors.newFixedThreadPool(100)

    @Test
    fun `기본 크롤링 테스트`() {
        val startTime = System.currentTimeMillis()
        val result = crawlingExample.crawling("https://www.hiver.co.kr/products/120533732")
        val endTime = System.currentTimeMillis()
        println("크롤링 결과: ${result?.substring(0, 100)}...")
        println("실행 시간: ${endTime - startTime}ms")
    }

    @Test
    fun `멀티 스레딩 크롤링 테스트`() {
        val startTime = System.currentTimeMillis()
        val results = (1..10).map {
            executor.submit<String> {
                crawlingExample.crawling("https://www.hiver.co.kr/products/120533732")
            }
        }.map {
            it.get()
        }
        val endTime = System.currentTimeMillis()
        println("크롤링 결과 개수: ${results.size}개")
        println("실행 시간: ${endTime - startTime}ms")
    }

    @Test
    fun `논블로킹 크롤링 테스트`() {
        val startTime = System.currentTimeMillis()
        val urls = listOf(
            "https://www.hiver.co.kr/products/120533732",
            "https://www.hiver.co.kr/products/120533732"
        )
        val results = urls.map {
            crawlingExample.crawlingAsync(it)
        }

        CompletableFuture.allOf(*results.toTypedArray()).join()

        val endTime = System.currentTimeMillis()
        println("크롤링 결과 개수: ${results.size}개")
        println("실행 시간: ${endTime - startTime}ms")
    }

    @Test
    fun `논블로킹 크롤링에 RateLimiter 적용 테스트`() {
        val rateLimiter = RateLimiter.create(50.0)
        val startTime = System.currentTimeMillis()
        val urls = listOf(
            "https://www.hiver.co.kr/products/120533732",
            "https://www.hiver.co.kr/products/120533732"
        )
        val results = urls.map {
            rateLimiter.acquire(1000)
            crawlingExample.crawlingAsync(it)
        }

        CompletableFuture.allOf(*results.toTypedArray()).join()

        val endTime = System.currentTimeMillis()
        println("크롤링 결과 개수: ${results.size}개")
        println("실행 시간: ${endTime - startTime}ms")
    }

    @Test
    fun `콜백으로 예쁘게 만들어보기`() {
        crawlingExample.crawling("https://www.hiver.co.kr/products/120533732") {
            println("크롤링 결과: ${it.substring(0, 100)}")
        }
        Thread.sleep(3000)
    }

    @Test
    fun `콜백으로 예쁘게 만들어보기2`() {
        val urls = listOf(
            "https://www.hiver.co.kr/products/120533732",
            "https://www.hiver.co.kr/products/120533732"
        )
        crawlingExample.crawling(urls) {
            println("크롤링 결과: ${it.substring(0, 100)}")
        }
        Thread.sleep(3000)
    }
}
