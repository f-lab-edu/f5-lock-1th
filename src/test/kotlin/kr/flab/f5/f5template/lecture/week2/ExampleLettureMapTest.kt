package kr.flab.f5.f5template.lecture.week2

import kr.flab.f5.f5template.lecture.week4.RelieveSizeHashMap
import kr.flab.f5.f5template.lecture.week4.StrictSizeHashMap
import org.assertj.core.api.Assertions
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.test.Test

class ExampleLettureMapTest {

    private val threadCount = 100
    private val testSize = 1000

    private val executor = Executors.newFixedThreadPool(threadCount)

    private val stringArray = (0..testSize).map {
        it.toString()
    }

    private val intArray = (0..testSize).map {
        it
    }

    @Test
    fun `일반 맵 테스트`() {
        val n = 1000
        val times = 10
        var sum = 0L

        (1..times).forEach {

            val latch = CountDownLatch(threadCount)
            val start = System.currentTimeMillis()
            val lecture1HashMap = Lecture1HashMap<String, Int>()

            (1..threadCount).forEach { it1 ->

                executor.submit {
                    (1..n).forEach { it2 ->

                        lecture1HashMap.put(it1.toString(), it2)
                        if(lecture1HashMap.get(it1.toString()) != it2) {
                            println("불일치 발생")
                        }
                    }
                    latch.countDown()
                }
            }

            latch.await()
            val end = System.currentTimeMillis()
            sum += (end - start)
        }

        println("실행 시간: ${sum / times}ms - 시간 테스트")
    }

    @Test
    fun `get put 에 싱크 걸어 테스트`() {
        val n = 1000
        val times = 10
        var sum = 0L

        (1..times).forEach {

            val latch = CountDownLatch(threadCount)
            val start = System.currentTimeMillis()
            val lecture2HashMap = Lecture2HashMap<Int, Int>()

            (1..threadCount).forEach {

                executor.submit {
                    val random = Random.nextInt().absoluteValue
                    (1..n).forEach { it2 ->

                        lecture2HashMap.put(random, it2 * it)
                        if(lecture2HashMap.get(random) != it2 * it) {
                            println("불일치 발생")
                        }
                    }

                    latch.countDown()
                }
            }

            latch.await()

            val end = System.currentTimeMillis()
            sum += (end - start)
        }

        println("실행 시간: ${sum / times}ms - 시간 테스트")
    }

    @Test
    fun `내 맵 테스트`() {
        val n = 1000
        val times = 10
        var sum = 0L

        (1..times).forEach {
            val latch = CountDownLatch(threadCount)
            val start = System.currentTimeMillis()
            val lecture3HashMap = Lecture3HashMap<Int, Int>(50)

            (1..threadCount).forEach {

                executor.submit {
                    val random = Random.nextInt().absoluteValue
                    (1..n).forEach { it2 ->
                        lecture3HashMap.put(random, it2 * it)
                        if(lecture3HashMap.get(random) != it2 * it) {
                            println("불일치 발생")
                        }
                    }

                    latch.countDown()
                }
            }

            latch.await()

            val end = System.currentTimeMillis()
            sum += (end - start)
        }

        println("실행 시간: ${sum / times}ms - 시간 테스트")

    }

    @Test
    fun concurrentTest() {
        val n = 1000000
        var start = System.currentTimeMillis()

        val strictSizeHashMap = StrictSizeHashMap<Int, String>()
        val relieveSizeHashMap = RelieveSizeHashMap<Int, String>()
        val hashMap = HashMap<Int, String>()
        val concurrentHashMap = ConcurrentHashMap<Int, String>()
        var countDownLatch = CountDownLatch(n)

        for(i in 0..n-1) {
            executor.submit {
                hashMap.putIfAbsent(i, "Thread")
                hashMap[i] = "Thread1"
                hashMap.remove(i)
                countDownLatch.countDown()
            }
        }

        countDownLatch.await()

        println(hashMap.size)
        var end = System.currentTimeMillis()
        println("실행 시간 ${end - start}ms")
        start = System.currentTimeMillis()

        countDownLatch = CountDownLatch(n)

        for(i in 0..n-1) {
            executor.submit {
                concurrentHashMap.putIfAbsent(i, "Thread")
                concurrentHashMap[i] = "Thread1"
                concurrentHashMap.remove(i)
                countDownLatch.countDown()
            }
        }

        countDownLatch.await()

        end = System.currentTimeMillis()
        println("concurrent 실행 시간 ${end - start}ms")
        Assertions.assertThat(concurrentHashMap.size).isEqualTo(0)

        start = System.currentTimeMillis()

        countDownLatch = CountDownLatch(n)

        for(i in 0..n-1) {
            executor.submit {
                relieveSizeHashMap.putIfAbsent(i, "Thread")
                relieveSizeHashMap.put(i, "Thread1")
                relieveSizeHashMap.remove(i)
                countDownLatch.countDown()
            }
        }

        countDownLatch.await()

        end = System.currentTimeMillis()
        println("relieve 실행 시간 ${end - start}ms")
        Assertions.assertThat(relieveSizeHashMap.size()).isEqualTo(0)

        start = System.currentTimeMillis()

        countDownLatch = CountDownLatch(n)

        for(i in 0..n-1) {
            executor.submit {
                strictSizeHashMap.putIfAbsent(i, "Thread")
                strictSizeHashMap.put(i, "Thread1")
                strictSizeHashMap.remove(i)
                countDownLatch.countDown()
            }
        }

        countDownLatch.await()

        end = System.currentTimeMillis()
        println("strict 실행 시간 ${end - start}ms")
        Assertions.assertThat(strictSizeHashMap.size()).isEqualTo(0)
    }
}