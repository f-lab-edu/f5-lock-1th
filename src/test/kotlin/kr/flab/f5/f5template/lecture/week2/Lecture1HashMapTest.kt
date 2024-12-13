package kr.flab.f5.f5template.lecture.week2

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class Lecture1HashMapTest {

    @Test
    fun `동시에 putIfAbsent 호출해도 단 1개만 put성공`() {
        //given
        val optimisticLockMap = Lecture1HashMap<String, Int>()
        val executor = Executors.newFixedThreadPool(3)
        val KEY = "key1";
        val successCount = AtomicInteger(0)
        //when
        val tasks = listOf(
            Runnable {
                val result =  optimisticLockMap.putIfAbsent(KEY, 1)
                if (result == null) successCount.incrementAndGet()
            },
            Runnable {
                val result = optimisticLockMap.putIfAbsent(KEY, 2)
                if (result == null) successCount.incrementAndGet()
            },
            Runnable {
                val result = optimisticLockMap.putIfAbsent(KEY, 3)
                if (result == null) successCount.incrementAndGet()
            },
        );
        tasks.forEach { executor.submit(it) }
        executor.shutdown()
        executor.awaitTermination(2, TimeUnit.SECONDS)

        //then
        assertEquals(successCount.get(), 1)
    }


    @Test
    fun `동시에 put 호출해도 정상차감`() {
        //given
        val optimisticLockMap = Lecture1HashMap<String, Int>()
        val executor = Executors.newFixedThreadPool(2)
        val KEY = "key1";
        optimisticLockMap.put(KEY,3);

        //when
        val tasks = listOf(
            Runnable {
                val count = optimisticLockMap[KEY] ?: 0
                val newCount  = count - 1;
                optimisticLockMap.put(KEY, newCount)
            },
            Runnable {
                val count = optimisticLockMap[KEY] ?: 0
                val newCount  = count - 1;
                optimisticLockMap.put(KEY, newCount)
            },
            Runnable {
                val count = optimisticLockMap[KEY] ?: 0
                val newCount  = count + 1;
                optimisticLockMap.put(KEY, newCount)
            },
        );
        tasks.forEach { executor.submit(it) }
        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.SECONDS)

        //then
        assertEquals(2, optimisticLockMap[KEY])
    }


}