package kr.flab.f5.f5template.lecture.week3.spinlock;

import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.locks.ReentrantLock

/**
 * SpinLocks 클래스
 * ConcurrentHashMap과 같이 N개의 락이 필요한 경우
 * 스핀락으로 제어하도록 구현했습니다
 */
class SpinLocks(defaultSize: Int = 17, fair: Boolean = false) : MultiLockable {

    private val locks = Array(defaultSize) { ReentrantLock(fair) }

    private fun getLock(index: Int): ReentrantLock {
        if (index < 0 || index >= locks.size) throw IndexOutOfBoundsException()
        return locks[index]
    }

    override fun lock(index: Int) {
        val lock = getLock(index)
        while (true) {
            if (lock.tryLock()) {
                break
            }
        }
    }

    override fun lock(index: Int, timeout: Long) {
        val lock = getLock(index)
        val deadline = System.currentTimeMillis() + TimeUnit.MICROSECONDS.toMillis(timeout)

        while (System.currentTimeMillis() < deadline) {
            if (lock.tryLock()) {
                break
            }
        }
        throw TimeoutException("${timeout}ms 안에 락을 획득 못했습니다")
    }

    override fun unlock(index: Int) {
        val lock = getLock(index)
        if (lock.isLocked) {
            lock.unlock()
            return
        }
        throw IllegalMonitorStateException("잠금된 객체가 없는 상태에서 잠금 해제 시도했습니다")
    }

    override fun isLocked(index: Int): Boolean = getLock(index).isLocked

    override fun allLock() {
        locks.forEach {
            it.lock()
        }
    }

    override fun allUnlock() {
        locks.forEach {
            it.unlock()
        }
    }

    override fun size() = locks.size
}
