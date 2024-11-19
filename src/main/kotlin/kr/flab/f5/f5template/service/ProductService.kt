package kr.flab.f5.f5template.service

import kr.flab.f5.f5template.controller.request.CreateProductRequest
import kr.flab.f5.f5template.controller.response.ProductResult
import kr.flab.f5.f5template.mysql.jpa.entity.Product
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.springframework.stereotype.Service

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

    fun createProduct(request: CreateProductRequest): ProductResult {
        val product = Product(
            name = request.name,
            price = request.price,
            stock = request.stock
        )
        productRepository.save(product)
        return ProductResult(
            id = product.id,
            name = product.name
        )
    }
}
