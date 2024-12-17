package kr.flab.f5.f5template.lecture.week2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class Lecture1HashMapTest {

    @Test
    fun `동시성_PUT_테스트`() {
        val executorService = Executors.newFixedThreadPool(10)
        val myHashMap = Lecture1HashMap<String, Int>()
        val executeCount = 10000
        val countDownLatch = CountDownLatch(executeCount)
        var num = 0

        val start = System.currentTimeMillis()

        repeat(executeCount) {
            val index = num++
            executorService.submit {
                myHashMap.put("String$index", num)
                countDownLatch.countDown()
            }
        }

        countDownLatch.await()

        val end = System.currentTimeMillis()
        println("실행 시간: ${end - start}ms")
        assertEquals(executeCount, myHashMap.size())
    }
}