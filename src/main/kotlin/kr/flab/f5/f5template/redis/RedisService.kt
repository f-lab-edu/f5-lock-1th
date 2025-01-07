package kr.flab.f5.f5template.redis

import java.time.Duration

interface RedisService<K, V> {

    fun get(key: K): V?

    fun set(key: K, value: V, ttl: Duration)

    fun delete(key: K)
}