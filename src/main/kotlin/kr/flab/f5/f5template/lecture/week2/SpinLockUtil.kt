import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/**
 * SpinLockUtil을 사용하는 관점에서 SpinLockUtil이 여러개의 인스턴스로 만들어지면 이를 사용하는 코드에서는 개발자의 실수로 SpinLockUtil여러개를 생성해서 사용할 수 있다.
 * 이런 경우에는 동시성보장이 힘들다. 따라서 싱글톤으로 객체생성을 진행하도록 했다. 
 */
class SpinLockUtil<T> private constructor(private val lockSize: Int) {
    private val locks = Array(lockSize) { AtomicReference<Thread?>(null) }

    companion object {
        private var instance: SpinLockUtil<Any>? = null

        @Synchronized
        @Suppress("UNCHECKED_CAST")
        fun <T> getInstance(lockSize: Int = 16): SpinLockUtil<T> {
            if (instance == null || instance!!.lockSize != lockSize) {
                instance = SpinLockUtil(lockSize)
            }
            return instance as SpinLockUtil<T>
        }
    }

    private fun getLockIndex(key: Any?): Int {
        return key.hashCode() % lockSize
    }

    private fun getLock(key: Any?): AtomicReference<Thread?> {
        val index = getLockIndex(key)
        return locks[index]
    }

    fun acquireLock(
        key: Any?,
        maxRetryCount: Int = -1,
        timeout: Long = 0,
        unit: TimeUnit = TimeUnit.MILLISECONDS
    ): Boolean {
        val lock = getLock(key)
        var retryCount = 0
        while (retryCount < maxRetryCount || maxRetryCount == -1) {
            if (lock.compareAndSet(null, Thread.currentThread())) {
                return true
            }
            if (timeout > 0) {
                Thread.sleep(unit.toMillis(timeout))
            }
            retryCount++
        }
        return false
    }

    fun releaseLock(key: Any?) {
        val lock = getLock(key)
        if (lock.get() !== Thread.currentThread()) {
            throw Exception("release 실패, lock을 가지고 있지 않습니다.")
        }
        lock.set(null)
    }

    fun <R> withLock(key: Any?, block: () -> R): R? {
        try {
            if (acquireLock(key)) {
                return block()
            }
        } finally {
            releaseLock(key)
        }
        return null
    }
}
