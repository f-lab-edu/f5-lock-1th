package kr.flab.f5.f5template.lecture.week2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore

class Lecture1HashMapTest {

    private val executors = Executors.newCachedThreadPool()
    private val executionCount = 10000

    @DisplayName("멀티스레드 환경에서 put할 때, thread safe를 검증한다")
    @Test
    fun threadSafe() {
        val concurrencyMap = Lecture1HashMap<Int, String>()
        val countDownLatch = CountDownLatch(executionCount);

        repeat(executionCount) {
            executors.submit {
                concurrencyMap.put(it, "값 ${it}")
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
        executors.shutdown()

        assertThat(concurrencyMap.size()).isEqualTo(executionCount)
    }

    @DisplayName("put 실행 시간에 따른 성능 측정을 확인한다")
    @Test
    fun measurePutExecutionTime() {
        val cyclicBarrier = CyclicBarrier(executionCount + 1)
        val iterations = 1
        var totalTime = 0L

        repeat(iterations) {
            val concurrencyMap = Lecture1HashMap<Int, String>()

            val startTime = System.currentTimeMillis()
            repeat(executionCount) {
                executors.submit {
                    cyclicBarrier.await()
                    concurrencyMap.put(it, "value : ${it}")
                }
            }
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime

            cyclicBarrier.await()
            println("${it + 1}회차, 실행시간 : ${duration}ms")
            totalTime += duration
        }
        println("put 평균 실행 시간 : ${totalTime / iterations}ms")
    }

    @DisplayName("동시에 실행할 요청의 개수를 제한하여 put 실행 시간을 측정한다")
    @Test
    fun useSemaphore() {
        val permits = 10
        val semaphore = Semaphore(permits)
        val barrier = CyclicBarrier(executionCount + 1)
        val concurrencyMap = Lecture1HashMap<Int, String>()

        val startTime = System.currentTimeMillis()
        repeat(executionCount) {
            executors.submit {
                barrier.await()
                semaphore.acquire()
                concurrencyMap.put(it, "value : ${it}")
                semaphore.release()
            }
        }
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        barrier.await()
        println("동시요청 ${permits}개로 제한한 실행시간은 ${duration}ms")
    }

//    @DisplayName("put이 지속적으로 일어나고 있을때 get을 해도 thread safe 하다")
//    @Test
//    fun putAndGet() {
//        val executionCount = 400
//        val concurrencyMap = Lecture1HashMap<Int, Int>()
//        val cyclicBarrier = CyclicBarrier(executionCount * 2 + 1)
//
//        repeat(executionCount){
//            executors.submit {
//                cyclicBarrier.await()
//                //println("put ${it}start")
//                concurrencyMap.put(it, it)
//                //println("put ${it} end")
//            }
//        }
//
//        repeat(executionCount){
//            executors.submit {
//                cyclicBarrier.await()
//                //println("get ${it} start")
//                assertNotNull(concurrencyMap.get(it))
//                //println("get ${it} end")
//            }
//        }
//
//        cyclicBarrier.await()
//    }

}