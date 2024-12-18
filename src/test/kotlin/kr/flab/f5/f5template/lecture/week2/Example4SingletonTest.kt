package kr.flab.f5.f5template.lecture.week2

import org.assertj.core.api.Assertions.assertThat
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread
import kotlin.test.Test

class Example4SingletonTest {

    private val executor = Executors.newFixedThreadPool(100)

    @Test
    fun `싱글톤은 1개의 객체만 만들어져야 한다 CountDownLatch Version`() {
        val n = 1000
        val latch = CountDownLatch(1000)
        val futures = (1..n).map {
            executor.submit<Example4Singleton> {
                latch.countDown()
                Example4Singleton.getInstance()
            }
        }

        latch.await()

        val objects = futures.map {
            it.get()
        }.distinct()

        assertThat(objects.size).isEqualTo(1)
    }

    @Test
    fun `싱글톤은 1개의 객체만 만들어져야 한다 Semaphore Version`() {
        val n = 100
        val semaphore = Semaphore(10)
        var count = 0
        val singletons = mutableListOf<Example4Singleton>()
        val workers = mutableListOf<Thread>()

        repeat(n) {id ->
            val worker = thread {
                semaphore.acquire()
                singletons.add(Example4Singleton.getInstance())
                println("Working acquire $id")
                count++
                Thread.sleep(10)
                semaphore.release()
                println("Working release $id")
            }
            workers.add(worker)
        }

        workers.forEach { it.join() }

        val objects = singletons.distinct()

        assertThat(objects.size).isEqualTo(1)
        assertThat(count).isEqualTo(n)
    }

    @Test
    fun `싱글톤은 1개의 객체만 만들어져야 한다 CycleBarrier Version`() {
        val n = 1000
        val singletons = mutableListOf<Example4Singleton>()
        val workers = mutableListOf<Thread>()
        var count = 0

        val cycleBarrier = CyclicBarrier(n) {
            println("All cycleBarrier is ended")
        }

        repeat(n) { id ->
            val worker = thread {
                singletons.add(Example4Singleton.getInstance())
                count++
                cycleBarrier.await() // 바리어에 도착
            }
            workers.add(worker)
        }


        workers.forEach { it.join() }
        println("All workers wait here")
        val objects = singletons.distinct()

        assertThat(objects.size).isEqualTo(1)
        assertThat(count).isEqualTo(n)
    }
}
