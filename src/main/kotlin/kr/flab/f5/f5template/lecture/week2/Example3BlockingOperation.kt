package kr.flab.f5.f5template.lecture.week2

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.CompletableFuture

@Component
class Example3BlockingOperation(
    @Qualifier("singleConnectionWebClient")
    private val webClient: WebClient,
    private val restTemplate: RestTemplate,
) {

    fun blockingCrawlingWithRestTemplate(url: String): String? {
        return restTemplate.getForObject(url, String::class.java)
    }

    fun blockingCrawling(url: String): String? {
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
    }

    fun nonBlockingCrawling(url: String): CompletableFuture<String> {
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(String::class.java)
            .toFuture()
    }
}
