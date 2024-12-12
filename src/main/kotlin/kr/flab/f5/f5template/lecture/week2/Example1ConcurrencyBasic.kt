package kr.flab.f5.f5template.lecture.week2

import org.springframework.stereotype.Component

@Component
class Example1ConcurrencyBasic(
    private var value: Long = 0L,
) {

    fun add(amount: Long) {
        value += amount
    }

    fun get(): Long {
        return value
    }
}
