package kr.flab.f5.f5template.lecture.week3

import kotlin.test.Test

class Example2ParallelStreamTest {

    val example2ParallelStream = Example2ParallelStream()

    @Test
    fun coroutine1Test() {
        example2ParallelStream.coroutine()
    }
}
