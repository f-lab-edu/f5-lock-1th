package kr.flab.f5.f5template.redis

import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.ReturnType
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class CustomCommandExample(
    // 로우한 레디스 명령어를 쓰고 싶다면 커넥션으로 직접 컨트롤이 가능합니다.
    private val redisConnectionFactory: RedisConnectionFactory,

    // 스프링에서 제공하는 레디스 템플릿을 사용하면 더 쉽게 레디스를 사용할 수 있습니다.
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun customCommand() {
        // 커넥션을 통해 레디스 명령어를 실행할 수 있습니다.
        val connection = redisConnectionFactory.connection
        connection.set("key".toByteArray(), "value".toByteArray())

        // 레디스 템플릿을 통해 레디스 명령어를 실행할 수 있습니다.
        // 참고 : https://docs.spring.io/spring-data/redis/reference/redis/template.html
        redisTemplate.opsForValue().set("key", "value")
    }

    fun luaScript() {
        // eval을 사용하면 lua 스크립트로 명령어를 묶어 atomic하게 실행할 수 있습니다.
        // 참고 : https://redis.io/commands/eval
        val script = """
            return redis.call('set', KEYS[1], ARGV[1])
        """.trimIndent()

        val connection = redisConnectionFactory.connection
        connection.eval<Int>(script.toByteArray(), ReturnType.INTEGER, 1, "key".toByteArray(), "value".toByteArray())
    }
}
