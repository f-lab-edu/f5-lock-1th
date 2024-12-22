package kr.flab.f5.f5template.lecture.week4

import kr.flab.f5.f5template.mysql.jpa.repository.OrderRepository
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class Example1TransactionBasic(
    private val jdbcTemplate: JdbcTemplate,
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
) {

    @Transactional
    fun jpaTransaction() {
        // exclusive
        val product = productRepository.findByIdOrNull(1L) ?: throw RuntimeException("Product not found")
        product.decreaseStock()
    }

    @Transactional
    fun jdbcTransaction() {
        /*
        val product = jdbcTemplate.queryForObject("SELECT * FROM product WHERE id = 1 FOR UPDATE")
        jdbcTemplate.update("UPDATE product SET stock = ? WHERE id = 1", product.stock - 1)

         */
    }
}
