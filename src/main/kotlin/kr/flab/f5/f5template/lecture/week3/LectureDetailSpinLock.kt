package kr.flab.f5.f5template.lecture.week3

import java.util.concurrent.atomic.AtomicInteger


// 그냥 만든 SpinLock 은 ReentrantLock 을 그냥 사용만 하는 느낌이라
// AtomicInteger 와 currentThread 를 이용하여 만들어 보고 있습니다.
// 방식은 ReentrantLock 의 내부를 참고하였습니다. 로직만 먼저 설명드리자면
// atomicInteger 를 하나 가지고 0일 때만 들어갈 수 있고 아닐 경우는 못들어가는것으로 판단합니다.
// ReentrantLock 은 같은 쓰레드인경우 이 값을 계속 플러스 할 수 있는 형태로 되어있었는데, 이 부분을 필요할지 고민해보려고 합니다.
enum class LockStatus(
    val status: Int
) {
    AVAILABLE(0)
}
class LectureDetailSpinLock {
    val currentLockCount = AtomicInteger(0)

    @Volatile
    var currentThread: Thread = Thread.currentThread()

    fun lock() {
        //락 점유가 가능한 상태
        if(currentLockCount.get() == LockStatus.AVAILABLE.status) {

        }
        else {

        }
    }
}