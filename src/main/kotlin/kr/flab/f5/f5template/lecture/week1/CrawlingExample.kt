package kr.flab.f5.f5template.lecture.week1

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.CompletableFuture

@Component
class CrawlingExample(
    private val defaultWebClient: WebClient,
) {

    fun crawling(url: String, callback: (String) -> Unit) {
        defaultWebClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(String::class.java)
            .subscribe(callback)
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
