package kr.flab.f5.f5template.lecture.week2

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class Example2ExternalApi(
    @Qualifier("defaultWebClient")
    private var webClient: WebClient,
) {
    @Volatile
    private var apiSecretToken: String? = null

    fun callApi(): String? {
        return webClient.get()
            .uri("/api")
            .headers { it.setBearerAuth(apiSecretToken ?: "") }
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
    }

    @Scheduled(fixedRate = 1000 * 20)
    fun updateApiSecret() {
        // 토큰 만료시간 60초
        this.apiSecretToken = webClient.get()
            .uri("/api/secret")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
    }
}
