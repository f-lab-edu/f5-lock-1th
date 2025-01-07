package kr.flab.f5.f5template.lecture.week3

import java.util.concurrent.locks.ReentrantLock

class LectureSpinLock {
    private val lock = ReentrantLock()

    fun lock(callBack: () -> Unit) {
        var checkCallBackException = false
        println("lock acquire try ${Thread.currentThread()}")

        try {
            while(!lock.tryLock()) { }
            checkCallBackException = true
            callBack()
        }
        finally {
            if(checkCallBackException) lock.unlock()
        }
    }

    // 지워질 메서드입니다
    // Exception 발생시 어떻게 동작할지에 대한 이해를 높이기 위해 만들었습니다
    fun lockException(callBack: () -> Unit) {
        var checkCallBackException = false
        println("lock acquire try ${Thread.currentThread()}")
        try {
            while(!lock.tryLock()) {
                println("lock acquire failCase ${Thread.currentThread()}")
                throw Exception("Exception 발생")
            }
            checkCallBackException = true
            callBack()
        }
        finally {
            if(checkCallBackException){
                println("Unlock thread id : ${Thread.currentThread()}")
                lock.unlock()
            }
            else {
                println("No Unlock thread id : ${Thread.currentThread()}")
            }
        }
    }

    // 지워질 메서드입니다
    // Exception 발생시 어떻게 동작할지에 대한 이해를 높이기 위해 만들었습니다
    fun lockExceptionInCallback(callBack: () -> Unit) {
        var checkCallBackException = false
        println("lock acquire try ${Thread.currentThread()}")
        try {
            while(!lock.tryLock()) { }
            checkCallBackException = true
            callBack()
            throw Exception("Exception 발생")
        }
        finally {
            if(checkCallBackException){
                println("Unlock thread id : ${Thread.currentThread()}")
                lock.unlock()
            }
            else {
                println("No Unlock thread id : ${Thread.currentThread()}")
            }
        }
    }
}