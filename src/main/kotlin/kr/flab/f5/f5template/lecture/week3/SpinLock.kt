package kr.flab.f5.f5template.lecture.week3

import java.util.concurrent.atomic.AtomicBoolean

class SpinLock {

    private val lock = AtomicBoolean(false)

    fun lock() {
        while (!lock.compareAndSet(false, true)) {
            println("lock 획득 실패")
            Thread.yield()
        }
        println("lock 획득 성공")
    }

    fun unlock() {
        lock.set(false)
    }
}