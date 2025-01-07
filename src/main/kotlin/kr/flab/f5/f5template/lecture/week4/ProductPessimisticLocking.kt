package kr.flab.f5.f5template.lecture.week4

import kr.flab.f5.f5template.mysql.jpa.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import javax.persistence.LockModeType

interface ProductPessimisticLocking : JpaRepository<Product, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    fun findExclusiveLock(id: Long): Product?
}