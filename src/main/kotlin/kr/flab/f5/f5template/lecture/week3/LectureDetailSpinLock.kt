package kr.flab.f5.f5template.lecture.week3

import java.util.concurrent.atomic.AtomicInteger

class LectureDetailSpinLock {
    val currentLockCount = AtomicInteger(0)

    private fun lock(): Boolean {
        synchronized(this) {
            val lockCount = currentLockCount.get()

            //락 점유가 가능한 상태
            if(lockCount == 0) {
                currentLockCount.compareAndSet(0, 1)
                return true
            }

            if(lockCount < 0) {
                throw Exception("락 카운트가 정상적인 상태가 아닙니다.")
            }
            return false
        }
    }

    private fun release(): Boolean {
        synchronized(this) {
            val lockCount = currentLockCount.get()

            if(lockCount > 0) {
                currentLockCount.compareAndSet(lockCount, lockCount - 1)
                return true
            }
            throw Exception("락 카운트가 정상적인 상태가 아닙니다.")

        }
    }

    fun spinLock(callBack: () -> (Unit)) {
        var checkCallBackException = false
        println("lock acquire try ${Thread.currentThread()}")
        try {
            while(!lock()) { }
            checkCallBackException = true
            callBack()
        }
        finally {
            if(checkCallBackException){
                println("Unlock thread id : ${Thread.currentThread()}")
                release()
            }
            else {
                println("No Unlock thread id : ${Thread.currentThread()}")
            }
        }
    }
}