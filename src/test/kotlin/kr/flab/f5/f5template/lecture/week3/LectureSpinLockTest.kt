package kr.flab.f5.f5template.lecture.week3

import java.util.concurrent.Executors
import kotlin.test.Test
import org.assertj.core.api.Assertions.assertThat
import java.util.concurrent.CountDownLatch
import kotlin.random.Random

class LectureSpinLockTest {

    private val executors = Executors.newFixedThreadPool(10)

    @Test
    fun lectureSpinLockRaceConditionTest() {
        val testCase = 20
        val spinLock = LectureSpinLock()
        var count = 0
        val latch = CountDownLatch(testCase)

        repeat(testCase) {
            executors.execute {
                try {
                    Thread.sleep(500)
                    spinLock.lock {
                        println("Working start Thread ${Thread.currentThread()}")
                        Thread.sleep(500)
                        count++
                        println("Working end Thread ${Thread.currentThread()}")
                    }
                }
                finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        assertThat(count).isEqualTo(20)
    }

    @Test
    fun lectureSpinLockExceptionTest() {
        val testCase = 40
        val spinLock = LectureSpinLock()
        val latch = CountDownLatch(testCase)

        repeat(testCase) {
            val randomNum = Random.nextInt()
            if(randomNum % 2 == 0) {
                executors.execute {
                    Thread.sleep(500)
                    spinLock.lock {
                        println("Working start Thread ${Thread.currentThread()}")
                        Thread.sleep(500)
                        println("Working end Thread ${Thread.currentThread()}")
                    }
                    latch.countDown()
                }
            }
            else {
                executors.execute {
                    try {
                        Thread.sleep(500)
                        spinLock.lockException {
                            println("Working start Thread ${Thread.currentThread()}")
                            Thread.sleep(500)
                            println("Working end Thread ${Thread.currentThread()}")
                        }

                    }
                    finally {
                        latch.countDown()
                    }

                }
            }
        }

        latch.await()
    }

    @Test
    fun lectureSpinLockExceptionInCallbackTest() {
        val testCase = 40
        val spinLock = LectureSpinLock()
        val latch = CountDownLatch(testCase)

        repeat(testCase) {
            val randomNum = Random.nextInt()
            if(randomNum % 2 == 0) {
                executors.execute {
                    Thread.sleep(500)
                    spinLock.lock {
                        println("Working start Thread ${Thread.currentThread()}")
                        Thread.sleep(500)
                        println("Working end Thread ${Thread.currentThread()}")
                    }
                    latch.countDown()
                }
            }
            else {
                executors.execute {
                    try {
                        Thread.sleep(500)
                        spinLock.lockExceptionInCallback {
                            println("Working start Thread ${Thread.currentThread()}")
                            Thread.sleep(500)
                            println("Working end Thread ${Thread.currentThread()}")
                        }

                    }
                    finally {
                        latch.countDown()
                    }

                }
            }
        }

        latch.await()
    }
}