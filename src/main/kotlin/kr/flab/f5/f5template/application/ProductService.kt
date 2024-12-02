package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.application.dto.ProductDto
import kr.flab.f5.f5template.application.exception.ProductNotFoundException
import kr.flab.f5.f5template.application.request.ProductCreateRequest
import kr.flab.f5.f5template.application.request.ProductUpdateRequest
import kr.flab.f5.f5template.mysql.jpa.entity.Product
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    // 데이터베이스 없이 순수 자바로 동시성 이슈를 다뤄보기 위해 아직은 데이터베이스를 사용하지 않습니다.
    private val productRepository: ProductRepository,
) {

    private val stock = HashMap<Long, Int>()

    init {
        stock[1] = 1000000
        stock[2] = 1000000
        stock[3] = 1000000
    }

    fun decreaseStock(id: Long) {
        val currentStock = stock[id] ?: throw IllegalArgumentException("No stock for product $id")
        if (currentStock <= 0) {
            throw IllegalArgumentException("No stock for product $id")
        }
        stock[id] = currentStock - 1
    }

    fun createStock(request: ProductCreateRequest): ProductDto {
        val product = Product(
            name = request.name,
            price = request.price,
            stock = request.stock,
        )

        return ProductDto.Companion.fromEntity(productRepository.save(product))
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Product? {
        return productRepository.findById(id)
            .orElseThrow { ProductNotFoundException(id) }
    }

    fun updateProduct(request: ProductUpdateRequest) {
        val product = productRepository.findById(request.id)
            .orElseThrow { ProductNotFoundException(request.id) }
        product.update(request.name, request.price, request.stock)
    }

    fun deleteProduct(productId: Long) {
        val product = productRepository.findById(productId)
            .orElseThrow { ProductNotFoundException(productId) }

        productRepository.delete(product)
    }
}
