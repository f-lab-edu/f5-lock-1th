package kr.flab.f5.f5template.redis

import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.ReturnType
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

/*
    val은 JAVA에서의 final과 같은 키워드로, JAVA Spring에서 Singleton + 생성자 주입을 사용할 때 private final로 컴포넌트 객체를 정의해 사용하는 것과
    비슷한 경우이다.
    일반적으로 val을 썼을 때는 변수의 타입을 꼭 명시해줘야한다. 다만, private val str = "String"과 같이 compile 타임에 타입이 유추 가능한 경우
    타입을 명시하지 않아도 된다.

    + val과 다르게 var 키워드도 있는데, javascript에서의 var과 같이 할당한 값을 바꿀 수 있게 선언하는 키워드이다.
    다만, 초기에 지정한 타입 이외의 값을 넣기 위해서는 형변환을 거쳐야한다. 다른 타입의 값을 담을 수 없다.
    다른 타입의 값을 담으려고 시도할 경우, TypeMismatch Exception이 발생한다.
 */
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
