package kr.flab.f5.f5template.lecture.week3

import java.util.concurrent.atomic.AtomicBoolean

/**
 * 스핀락은 lock을 얻지 못하면 블로킹 처리가 되지 않고 락을 얻을 때까지 계속해서 시도한다.
 * 이로 인해 CPU 사용량이 많아지게 되고 부하를 일으키게된다.
 * 이러한 일을 방지하기 위해 Thread.yield()을 활용했다.
 */
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