package kr.flab.f5.f5template.lecture.week3.spinlock

import java.util.concurrent.TimeUnit

interface MultiLockable {
    fun lock(index: Int)
    fun lock(index: Int, timeout: Long)
    fun unlock(index: Int)
    fun isLocked(index: Int): Boolean
    fun allLock()
    fun allUnlock()
    fun size(): Int
}