package kr.flab.f5.f5template.lecture.week3

import java.util.concurrent.atomic.AtomicReference

class CustomSpinLock (
    private val maxRetries: Int = 1000,
    private val timeoutMillis: Long = 50L) {
    // 현재 락을 소유한 스레드 (없으면 null)
    private val owner = AtomicReference<Thread?>(null)
    private val countMap = mutableMapOf<Thread, Int>()

    private fun lock() {
        val currentThread = Thread.currentThread()

        if(owner.get() == currentThread) {
            synchronized(this) {
                countMap[currentThread] = countMap.getOrDefault(currentThread, 0) + 1
            }
        }

        val spinStartTime = System.nanoTime()
        var retryCount = 0

        while(true) {
            if(owner.compareAndSet(null, currentThread)) {
                synchronized(this) {
                    countMap[currentThread] = 1
                }
                return
            }

            retryCount++
            if (retryCount > maxRetries) {
                throw IllegalStateException("SpinLock lock() 실패: 재시도($maxRetries) 초과")
            }

            val elapsedNanos = System.nanoTime() - spinStartTime
            if (elapsedNanos > timeoutMillis * 1_000_000) {
                throw InterruptedException("SpinLock lock() 실패: $timeoutMillis ms 타임아웃 초과")
            }

            Thread.yield()
        }
    }

    private fun unlock() {
        val current = Thread.currentThread()
        if (owner.get() != current) {
            throw IllegalMonitorStateException("현재 스레드가 락의 소유자가 아닙니다.")
        }

        synchronized(this) {
            val oldCount = countMap[current] ?: 0
            val newCount = oldCount - 1
            if (newCount > 0) {
                countMap[current] = newCount
            } else {
                countMap.remove(current)
                owner.set(null)
            }
        }
    }

    fun <T> withLock(block: () -> T): T {
        lock()
        return try {
            block()
        } finally {
            unlock()
        }
    }
}