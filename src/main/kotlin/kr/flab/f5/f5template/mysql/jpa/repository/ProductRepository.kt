package kr.flab.f5.f5template.mysql.jpa.repository

import kr.flab.f5.f5template.mysql.jpa.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun existsProductByName(name: String): Boolean
}
