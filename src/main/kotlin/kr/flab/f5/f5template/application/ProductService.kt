package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.common.exception.ExceptionCode.PRODUCT_NOT_FOUND
import kr.flab.f5.f5template.common.exception.ProductException
import kr.flab.f5.f5template.mysql.jpa.entity.Product
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ProductService(
    // 데이터베이스 없이 순수 자바로 동시성 이슈를 다뤄보기 위해 아직은 데이터베이스를 사용하지 않습니다.
    private val productRepository: ProductRepository,
) : ProductCommandUseCase, ProductReadUseCase {

    private val stock = HashMap<Long, Int>()

    init {
        stock[1] = 1000000
        stock[2] = 1000000
        stock[3] = 1000000
    }

    override fun decreaseStock(id: Long) {
        val foundProduct = findProductById(id)

        foundProduct.decreaseStock()
        productRepository.save(foundProduct)
    }

    override fun createProduct(product: ProductVO) {
        productRepository.save(product.toProduct())
    }

    override fun updateProduct(id: Long, product: ProductVO) {
        val foundProduct = findProductById(id)
        foundProduct.updateProduct(product.toProduct())

        productRepository.save(foundProduct)
    }

    override fun findProduct(id: Long): ProductVO {
        val product = findProductById(id)
        return ProductVO.from(product)
    }

    override fun deleteProduct(id: Long) {
        val product = findProductById(id)
        productRepository.delete(product)
    }

    fun findProductById(id:Long): Product {
        return productRepository.findById(id).getOrNull() ?: throw ProductException(
            PRODUCT_NOT_FOUND,
            "${PRODUCT_NOT_FOUND.cause}$id"
        )
    }
}
