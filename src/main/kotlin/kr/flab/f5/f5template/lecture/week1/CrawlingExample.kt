package kr.flab.f5.f5template.lecture.week1

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

@Component
class CrawlingExample(
    private val defaultWebClient: WebClient,
) {

    fun crawling(url: String): String? {
        return defaultWebClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
    }

    fun crawling(url: String, callback: (String) -> Unit) {
        defaultWebClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(String::class.java)
            .subscribe(callback)
    }

    fun crawlingAsync(url: String): CompletableFuture<String> {
        return defaultWebClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(String::class.java)
            .toFuture()
    }

    fun <T> crawling(urls: List<String>, callback: (String) -> T): List<CompletableFuture<T>> {
        return urls.map { url ->
            val future = CompletableFuture<T>()

            defaultWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String::class.java)
                .subscribe { body ->
                    future.complete(callback(body))
                }

            future
        }
    }
}
