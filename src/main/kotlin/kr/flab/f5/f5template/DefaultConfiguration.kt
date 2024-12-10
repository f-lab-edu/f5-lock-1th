package kr.flab.f5.f5template

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.util.concurrent.TimeUnit

@Configuration
class DefaultConfiguration {

    @Bean("defaultWebClient")
    fun defaultWebClient(): WebClient {
        val connectionProvider = ConnectionProvider.builder("myConnectionPool")
            .maxConnections(1000)
            .pendingAcquireMaxCount(1000000)
            .build()
        val httpClient = HttpClient.create(connectionProvider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30_000)
            .doOnConnected { connection ->
                connection.addHandlerLast(ReadTimeoutHandler(30_000, TimeUnit.MILLISECONDS))
                connection.addHandlerLast(WriteTimeoutHandler(30_000, TimeUnit.MILLISECONDS))
            }

        val objectMapper = ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val webClient = WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .codecs {
                it.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
                it.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
                it.defaultCodecs().maxInMemorySize(-1)
            }
            .build()

        return webClient
    }

    @Bean("singleConnectionWebClient")
    fun singleConnectionWebClient(): WebClient {
        val connectionProvider = ConnectionProvider.builder("myConnectionPool")
            .maxConnections(1)
            .pendingAcquireMaxCount(1000000)
            .build()
        val httpClient = HttpClient.create(connectionProvider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30_000)
            .doOnConnected { connection ->
                connection.addHandlerLast(ReadTimeoutHandler(30_000, TimeUnit.MILLISECONDS))
                connection.addHandlerLast(WriteTimeoutHandler(30_000, TimeUnit.MILLISECONDS))
            }

        val objectMapper = ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val webClient = WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .codecs {
                it.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
                it.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
                it.defaultCodecs().maxInMemorySize(-1)
            }
            .build()

        return webClient
    }

    @Bean
    fun restTemplate(): RestTemplate {
        val factory = HttpComponentsClientHttpRequestFactory()
        factory.setConnectTimeout(3000)
        factory.setReadTimeout(3000)

        val httpClient = HttpClientBuilder.create()
            .setMaxConnTotal(1)
            .setMaxConnPerRoute(1).build()
        factory.httpClient = httpClient

        return RestTemplate()
    }
}
