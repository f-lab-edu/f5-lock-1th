package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.mysql.jpa.entity.Product
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class ProductServiceTest{

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var productService: ProductService

    // 테스트용 값
    val INITIAL_STOCK = 10_000L
    var productId: Long = 0

    @BeforeEach
    fun setUp() {
        productRepository.deleteAll()

        val savedProduct = productRepository.save(
            Product(
                name = "Test Product",
                price = 1_000L,
                stock = INITIAL_STOCK
            )
        )
        productId = savedProduct.id
    }

    @Test
    fun `동시에 만 개의 재고 감소 요청 테스트`() {
        // Given
        val numberOfThreads = 10_000
        val latch = CountDownLatch(numberOfThreads)
        val executorService = Executors.newFixedThreadPool(1000)

        // When
        for (i in 1..numberOfThreads) {
            executorService.submit {
                try {
                    productService.decreaseStock(productId)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executorService.shutdown()

        // Then
        val product = productService.findById(productId)
        product?.let { assertEquals(0, it.stock, "최종 재고가 0이어야 합니다.") }
    }
}