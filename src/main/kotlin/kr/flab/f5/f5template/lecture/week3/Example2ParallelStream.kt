package kr.flab.f5.f5template.lecture.week3

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ForkJoinPool

class Example2ParallelStream {

    fun parallelStream() {
        val list = listOf("a", "b")
        list.parallelStream().forEach {
            // 병렬 처리
        }
        println("처리 끝")
    }


    fun parallelStreamThreadPoolSize() {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100")

        val list = listOf("a", "b")
        list.parallelStream().forEach {
            // 병렬 처리
        }
        println("처리 끝")
    }



    private val forkJoinPool = ForkJoinPool(100)

    fun parallelStreamThreadPoolSize2() {
        val list = listOf("a", "b")

        forkJoinPool.submit {
            list.parallelStream().forEach {
                // 병렬 처리
            }
        }.get()

        println("처리 끝")
    }



    fun coroutine() {
        val result = runBlocking {
            val result = (0..999).map {
                async(Dispatchers.Default) {
                    if (Math.random() * 1000 % 2 == 0.0) {
                        delay(1000)
                    }
                    println(it)
                    it + 1
                }
            }

            awaitAll(*result.toTypedArray())
        }

        println("처리 끝 $result")
    }
}
