package kr.flab.f5.f5template.lecture.week3

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.LinkedBlockingQueue

class Example1DataStructures {




    fun copyOnWrite() {
        val list = CopyOnWriteArrayList<String>()
    }








    fun blockingQueue() {
        val queue = LinkedBlockingQueue<String>(10)
        queue.put("자리가 날 때까지 기다린다")

        // 값이 없다면 들어올때까지 기다린다.
        val value = queue.take()
    }







    class SimpleExecutor(
        poolSize: Int = 10
    ) {

        private val taskQueue = LinkedBlockingQueue<Runnable>(poolSize)
        private val threadPool: List<Thread> = List(poolSize) {
            Thread {
                while (true) {
                    val task = taskQueue.take() // 스레드가 멈춰버린다
                    task.run()
                }
            }
        }

        init {
            threadPool.forEach { it.start() }
        }

        fun submit(job: Runnable) {
            taskQueue.put(job)
        }
    }
}
