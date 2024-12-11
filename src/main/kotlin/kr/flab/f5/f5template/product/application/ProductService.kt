package kr.flab.f5.f5template.product.application

import kr.flab.f5.f5template.mysql.jpa.entity.Product
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import kr.flab.f5.f5template.product.common.exception.Exception
import kr.flab.f5.f5template.product.common.exception.ExceptionCode
import kr.flab.f5.f5template.product.presentation.request.ProductRequest
import kr.flab.f5.f5template.product.presentation.response.ProductResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    fun findProducts(): List<ProductResponse> {
        val products = productRepository.findAll()

        return products.map(Product::toResponse)
    }

    @Transactional(readOnly = true)
    fun findProductById(id: Long): ProductResponse {
        val product = productRepository.findById(id)
            .orElseThrow { Exception(ExceptionCode.NOT_FOUND) }

        return product.toResponse()
    }

    @Transactional
    fun createProduct(request: ProductRequest) {
        val newProduct = Product(
            name = request.name,
            price = request.price,
            stock = request.stock
        )

        productRepository.save(newProduct)
    }

    @Transactional
    fun deleteProduct(id: Long) {
        val product = productRepository.findById(id)
            .orElseThrow { Exception(ExceptionCode.NOT_FOUND) }

        productRepository.delete(product)
    }

    @Transactional
    fun updateProduct(
        id: Long,
        request: ProductRequest
    ) {
        val product = productRepository.findById(id)
            .orElseThrow { Exception(ExceptionCode.NOT_FOUND) }

        product.update(
            name = request.name,
            price = request.price,
            stock = request.stock
        )
    }

}
