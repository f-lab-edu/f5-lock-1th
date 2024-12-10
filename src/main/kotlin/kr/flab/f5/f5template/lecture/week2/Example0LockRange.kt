package kr.flab.f5.f5template.lecture.week2

import java.util.concurrent.Executors

class Example0LockRange(
    private var value: Long = 0L,
) {
    val lockKey = Any()

    @Synchronized
    fun objectLock() {
        value++
    }

    fun blockLock() {
        synchronized(lockKey) {
            value++
        }
    }

    fun objectLock2() {
        val obj = Any()
        synchronized(obj) {
            // do something
        }
    }

    companion object { // 자바의 스태틱
        private val lock = Any()

        @Synchronized
        fun classLock() {
        }
    }
}
