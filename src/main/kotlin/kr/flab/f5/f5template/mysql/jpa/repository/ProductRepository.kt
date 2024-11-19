package kr.flab.f5.f5template.mysql.jpa.repository

import kr.flab.f5.f5template.mysql.jpa.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    override fun findById(id: Long): Optional<Product>;
    override fun deleteById(id: Long);
}
