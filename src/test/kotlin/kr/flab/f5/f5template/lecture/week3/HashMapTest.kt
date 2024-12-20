package kr.flab.f5.f5template.lecture.week3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class HashMapTest {

    private val myHashMap = SpinLockHashMap<String, Int>()
//    private val myHashMap = ReetrantLockHashMap<String, Int>()
//    private val myHashMap = CasHashMap<String, Int>()
    private val executorService = Executors.newFixedThreadPool(10)
    private val size = 1000
    private val latch = CountDownLatch(size)

    @Test
    fun `여러 스레드 동시 PUT 검증`() {
        for (i in 0 until size) {
            executorService.submit {
                myHashMap.put("key$i", i)
                latch.countDown()
            }
        }
        latch.await()
        assertEquals(size, myHashMap.size())
    }
}