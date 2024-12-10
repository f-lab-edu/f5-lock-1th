package kr.flab.f5.f5template.lecture.week2

class Example4Singleton {

    companion object {
        private var instance: Example4Singleton? = null

        // 락은 생성시에만 필요한거고, 다음에는 필요없다.
        fun getInstance(): Example4Singleton {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Example4Singleton()
                    }
                }
            }
            return instance!!
        }
    }
}
