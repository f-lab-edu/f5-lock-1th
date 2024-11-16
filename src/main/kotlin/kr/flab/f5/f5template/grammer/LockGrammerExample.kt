package kr.flab.f5.f5template.grammer

class LockGrammerExample {

    // Volatile 키워드는 자바와 다르게 어노테이션 방식입니다.
    @Volatile
    private var volatileVariable: Int = 0

    // Synchronized 키워드 또한 예약어가 아니라 어노테이션입니다.
    @Synchronized
    fun 메소드_레벨_락() {
        println("Synchronized method")
    }

    fun 객체_락() {
        val lock = Any()
        synchronized(lock) {
            println("Lock acquired")
        }
    }

    fun 자신에게_락_걸기() {
        synchronized(this) {
            println("Synchronized block")
        }
    }
}
