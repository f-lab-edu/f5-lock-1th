package kr.flab.f5.f5template.lecture.week4

import kr.flab.f5.f5template.mysql.jpa.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import javax.persistence.LockModeType

interface Example3PessimisticLocking : JpaRepository<Order, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    fun findSharedLock(id: Long): Order?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    fun findExclusiveLock(id: Long): Order?
}
