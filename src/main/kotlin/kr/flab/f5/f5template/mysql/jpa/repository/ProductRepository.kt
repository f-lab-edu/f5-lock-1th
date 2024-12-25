package kr.flab.f5.f5template.mysql.jpa.repository

import kr.flab.f5.f5template.mysql.jpa.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import javax.persistence.LockModeType

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    fun findByIdWithOutLock(id: Long): Product?

    // X-Lock (Exclusive Lock)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    fun findByIdWithXLock(id: Long): Product?

    // S-Lock (Shared Lock)
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    fun findByIdWithSLock(id: Long): Product?
}
