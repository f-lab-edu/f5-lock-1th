package kr.flab.f5.f5template.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisServiceImpl<K : Any,V : Any>(
    private val redisTemplate: RedisTemplate<K, V>
) : RedisService<K,V> {
    override fun get(key: K): V? {
        val operations = redisTemplate.opsForValue()
        return operations.get(key)
    }

    override fun set(key: K, value: V, ttl: Duration) {
        val operation = redisTemplate.opsForValue()

        operation.set(key, value, ttl)
    }

    override fun delete(key: K) {
        redisTemplate.delete(key)
    }
}