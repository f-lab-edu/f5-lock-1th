package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.application.dto.ProductCreateRequest
import kr.flab.f5.f5template.application.dto.ProductDto
import kr.flab.f5.f5template.application.dto.ProductUpdateRequest
import kr.flab.f5.f5template.error.BaseException
import kr.flab.f5.f5template.error.ErrorCode.PRODUCT_ALREADY_EXISTS
import kr.flab.f5.f5template.error.ErrorCode.PRODUCT_NOT_FOUND
import kr.flab.f5.f5template.mysql.jpa.entity.Product
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

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

    fun createStock(productCreateRequest: ProductCreateRequest) {

        if (productRepository.existsProductByName(productCreateRequest.name)) {
            throw BaseException(PRODUCT_ALREADY_EXISTS)
        }

        val product = Product(
            name = productCreateRequest.name,
            price = productCreateRequest.price,
            stock = productCreateRequest.stock,
        )

        productRepository.save(product)
    }

    fun findAllProducts(): List<ProductDto> {
        val products = productRepository.findAll()
        return products.map { product ->
            ProductDto(
                id = product.id,
                name = product.name,
                price = product.price,
                stock = product.stock
            )
        }
    }

    @Transactional
    fun updateProduct(productId: Long, updateRequest: ProductUpdateRequest) {
        val product = productRepository.findById(productId).orElseThrow { BaseException(PRODUCT_NOT_FOUND) }
        product.updateProduct(updateRequest.name, updateRequest.price, updateRequest.stock)
    }

    fun deleteProduct(productId: Long) {
        val product = productRepository.findById(productId).orElseThrow { BaseException(PRODUCT_NOT_FOUND) }
        productRepository.delete(product)
    }
}
