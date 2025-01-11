package kr.flab.f5.f5template.lecture.week4.mission

import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DecreaseStock(private val productRepository: ProductRepository) {


    @Transactional
    fun execute(id: Long) {
        val product = productRepository.findByIdWithLock(id) ?: throw NoSuchElementException("Product not found $id")
        if (product.stock <= 0) {
            throw IllegalStateException("No stock for product $id")
        }

        product.decreaseStock(1L)
    }
}
