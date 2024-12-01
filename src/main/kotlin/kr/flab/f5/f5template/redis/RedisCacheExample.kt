package kr.flab.f5.f5template.redis

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class RedisCacheExample {

    @Cacheable(value = ["cacheMethod"], key = "#name")
    fun cacheMethod(name: String): String {
        println("not cached!! $name")
        return "Hello, $name!"
    }
}
