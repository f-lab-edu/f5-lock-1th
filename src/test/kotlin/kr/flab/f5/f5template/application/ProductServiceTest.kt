package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.application.exception.ProductNotFoundException
import kr.flab.f5.f5template.application.request.ProductCreateRequest
import kr.flab.f5.f5template.application.request.ProductUpdateRequest
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@SpringBootTest
@Transactional
class ProductServiceTest(
    @Autowired
    private val productService: ProductService,
    @Autowired
    private val productRepository: ProductRepository
) {

    @DisplayName("상품을 추가한다")
    @Test
    fun create() {
        val request = createRequestWithStock(100)

        val saveProduct = productService.createStock(request)

        val productById = productRepository.findById(saveProduct.id)
        assertThat(productById)
            .isPresent
            .get()
            .satisfies { product ->
                assertThat(product.name).isEqualTo(request.name)
                assertThat(product.price).isEqualTo(request.price)
                assertThat(product.stock).isEqualTo(request.stock)
            }
    }

    @DisplayName("존재하지 않는 id로 상품 조회 시 예외 발생 한다")
    @Test
    fun findByIdThrowsException() {
        val request = createRequestWithStock(100)

        val saveProduct = productService.createStock(request)

        assertThrows(ProductNotFoundException::class.java) {
            productService.findById(saveProduct.id + 1)
        }
    }

    @DisplayName("상품 정보를 수정한다")
    @Test
    fun updateProduct() {
        val createRequest = ProductCreateRequest("품목이름", 10000, 100)
        val saveProduct = productService.createStock(createRequest)

        val updateRequest = ProductUpdateRequest(saveProduct.id, "품목이름변경", 15000, 150)
        productService.updateProduct(updateRequest)

        val productById = productRepository.findById(updateRequest.id)
        assertThat(productById)
            .isPresent
            .get()
            .satisfies { product ->
                assertThat(product.name).isEqualTo(updateRequest.name)
                assertThat(product.price).isEqualTo(updateRequest.price)
                assertThat(product.stock).isEqualTo(updateRequest.stock)
            }

    }

    @DisplayName("상품을 삭제한다")
    @Test
    fun deleteProduct() {
        val request = createRequestWithStock(100)
        val saveProduct = productService.createStock(request)

        productService.deleteProduct(saveProduct.id)

        assertThat(productRepository.findById(saveProduct.id)).isEmpty()
    }

    private fun createRequestWithStock(stock: Long): ProductCreateRequest {
        return ProductCreateRequest("품목이름", 10000, stock)
    }
}