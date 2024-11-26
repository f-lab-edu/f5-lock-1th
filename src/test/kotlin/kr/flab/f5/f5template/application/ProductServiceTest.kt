package kr.flab.f5.f5template.application

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private lateinit var productService: ProductService

    @Test
    fun createProductTest() {
        productService.create(10)
    }
}
