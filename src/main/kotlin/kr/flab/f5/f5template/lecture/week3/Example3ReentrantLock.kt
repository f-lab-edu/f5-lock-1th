package kr.flab.f5.f5template.lecture.week3

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

class Example3ReentrantLock(
    private var value: Long = 0L,
) {
    val lock = ReentrantLock()

    fun reentrantLock(a: String) {
        try {
            lock.lock()
            Integer.parseInt(a)
        } finally {
            lock.unlock()
        }
    }




    fun reentrantLockFairAndUnFair() {
        val lock2 = ReentrantLock(true) // 공정 모드
        val lock3 = ReentrantLock(false) // 불공정 모드
    }







    fun tryLock() {
        if (lock.tryLock()) {
            // do something
            lock.unlock()
        }
    }





    fun tryLock2() {
        if (lock.tryLock(300L, TimeUnit.MILLISECONDS)) {
            // do something
            lock.unlock()
        }
    }






    fun spinLock() {
        // 1. 락을 얻을때까지 무한 반복
        // 2. 락을 얻으며 루프를 빠져나옴
        while (true) {
            if (lock.tryLock()) {
                break
            }
        }
        // 로직
        lock.unlock()
    }






    fun spinLockExceptionHandling() {
        try {
            while (!lock.tryLock()) {
                // Do something
            }
        } finally {
            lock.unlock()
        }
    }
}
