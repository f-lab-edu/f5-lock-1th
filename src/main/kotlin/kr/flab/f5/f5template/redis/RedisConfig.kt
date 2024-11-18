package kr.flab.f5.f5template.redis

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@EnableCaching
@Configuration
class RedisConfig {

    // Spring Context로 부터 connectionFactory를 생성자 주입 받아 사용한다.
    @Bean
    fun redisCacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
        return RedisCacheManager.builder(connectionFactory)
            /*
                1. RedisCacheConfiguration 클래스로부터 기본 설정을 불러온다.
                2. Redis의 Key는 String, Value는 JSON 형식으로 사용하기위해 각각 Serializer를 설정해준다.(RedisSerializer 인터페이스를 구현한 객체들)
             */
            .cacheDefaults(
                RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofSeconds(30))
                    .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                            StringRedisSerializer()
                        )
                    )
                    .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                            GenericJackson2JsonRedisSerializer()
                        )
                    )
            )
            /*
              transactionAware
              출처 : https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/cache/RedisCacheManager.RedisCacheManagerBuilder.html
              Spring 프레임워크에서 트랜잭션을 수행할 때, RedisCache가 싱글스레드 synchronize 형식으로 cache를 생성하고 삭제할 수 있게 한다.
            */
            .transactionAware()
            /*
                출처 : https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/cache/RedisCacheManager.RedisCacheManagerBuilder.html
                초기화 때에 사용할 캐시 이름/RedisCacheConfiguration Pair를 추가한다.
            */
            .withInitialCacheConfigurations(
                mapOf(
                    "predefined" to RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()
                )
            )
            .build()
    }
}
