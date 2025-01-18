package kr.flab.f5.f5template.lecture.week4

import kr.flab.f5.f5template.mysql.jpa.entity.Product
import kr.flab.f5.f5template.mysql.jpa.repository.OrderRepository
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class Example1TransactionBasic(
    private val jdbcTemplate: JdbcTemplate,
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val productPessimisticLocking: ProductPessimisticLocking,
    private val productOptimisticRepository: ProductOptimisticRepository
) {

    @Transactional
    fun jpaTransaction() {
        // exclusive
        val product = productRepository.findByIdOrNull(1L) ?: throw RuntimeException("Product not found")
        product.decreaseStock()
    }

    @Transactional
    fun jdbcTransaction(productId: Long) {
        val product = jdbcTemplate.queryForObject<Product>("SELECT * FROM product WHERE id = 1 FOR UPDATE")
        jdbcTemplate.update("UPDATE product SET stock = ? WHERE id = 1", product.stock - 1)
    }

    @Transactional
    fun pessimisticLockingDecrease(productId: Long, amount: Long) {
        val product = productPessimisticLocking.findExclusiveLock(productId) ?: throw RuntimeException("Product not found")
        product.decreaseStock(amount)
    }

    @Transactional
    fun optimisticLockingDecrease(productId: Long, amount: Long) {
        val product = productOptimisticRepository.findByIdOrNull(productId) ?: throw RuntimeException("Product not found")
        product.decreaseStock(amount)
    }
}
