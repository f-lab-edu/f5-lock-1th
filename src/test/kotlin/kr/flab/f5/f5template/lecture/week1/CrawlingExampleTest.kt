package kr.flab.f5.f5template.lecture.week1

import com.google.common.util.concurrent.RateLimiter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicLong
import kotlin.test.Test

@SpringBootTest
class CrawlingExampleTest(
    @Autowired
    private val crawlingExample: CrawlingExample,
) {

    @Test
    fun `크롤링 테스트`() {
        val rateLimiter = RateLimiter.create(50.0)
        val urls = listOf("url1", "url2")
        // val results = crawlingExample.crawling(urls) {
        //     it.substring(0, 100)
        // }.map { it.get() }

        for (url in urls) {
            rateLimiter.acquire(1000)
            crawlingExample.crawling(url) {
                println("크롤링 결과: ${it.substring(0, 100)}...")
            }
        }
        Thread.sleep(100000)
    }
}
