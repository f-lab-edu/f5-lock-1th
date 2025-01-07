package kr.flab.f5.f5template.redis

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.Ordered
import org.springframework.core.StandardReflectionParameterNameDiscoverer
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.time.Duration

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RedisLock(
    val key: String,
    val name: String = "redisLock"
)

//해당 어노테이션의 우선순위
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Component
class RedisLockAspect(
    private val redisService: RedisService<String, String>
) {

    @Pointcut("@annotation(RedisLock)")
    fun lockAnnotation() {
    }

    @Around("lockAnnotation() && @annotation(redisLock)")
    fun around(joinPoint: ProceedingJoinPoint, redisLock: RedisLock) {
        val parameters = getParameterMapByName(joinPoint)

        require(parameters[redisLock.key] != null) {
            "parameter 와 일치하는 key 가 존재하지 않습니다 key : $redisLock.key"
        }

        val key = PREFIX_KEY + redisLock.key + parameters[redisLock.key]?.toString()

        //적당한 value 가 무엇이 좋을지 고민해봐야함. 사실 키값이 중요하고 value 는 별로 중요하지 않아 무엇을 넣어야 할지 고민됨
        val value = "$PREFIX_KEY$key"

        val startTime = System.currentTimeMillis()
        println(redisService.get(key))

        try {
            while (redisService.get(key) != null) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - startTime > WAITING_TIME) {
                    println("Lock 획득 실패")
                    return
                }

                println("lock 획득 시도")
            }

            redisService.set(key, value, Duration.ofMillis(RELEASE_TIME))
            joinPoint.proceed()

            val endTime = System.currentTimeMillis()

            if (endTime - startTime > RELEASE_TIME) {
                println("RELEASE TIME OUT")
                throw Exception("레디스 RELEASE TIME OUT")
            }

        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } finally {
            redisService.delete(key)
        }
        return
    }

    private fun getParameterMapByName(joinPoint: JoinPoint): Map<String, Any> {
        val parameterMapByName: MutableMap<String, Any> = mutableMapOf()
        // 파라미터의 값
        val args = joinPoint.args
        val signature = joinPoint.signature as MethodSignature
        // 메서드 정보
        val method = joinPoint.target.javaClass.getDeclaredMethod(
            signature.method.name,
            *signature.method.parameterTypes
        )
        // 파라미터의 이름
        val parameterNames =
            StandardReflectionParameterNameDiscoverer().getParameterNames(method) ?: return mutableMapOf()
        for (index in parameterNames.indices) {
            parameterMapByName[parameterNames[index]] = args[index]
        }
        return parameterMapByName
    }

    /**
     * 유연하게 대처하기 위해선 이 부분이 고정이 아닌 설정의 형태가 되어야 한다.
     * */
    companion object {
        const val WAITING_TIME = 5_000L
        const val RELEASE_TIME = 10_000L
        const val PREFIX_KEY = "LOCK_KEY_"
    }
}
