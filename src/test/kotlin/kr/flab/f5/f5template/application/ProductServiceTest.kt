package kr.flab.f5.f5template.application

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

const val NOT_EXIST_PRODUCT_ID = -1L

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private lateinit var productService: ProductService

    @Test
    fun createProductTest() {
        productService.createStock(10)
    }

    @Test
    fun searchProductSuccessTest() {
        val searchStock = productService.searchStock(1)
        assert(searchStock == DEFAULT_STOCK)
    }

    @Test
    fun searchProductFailTest() {
        try {
            productService.searchStock(NOT_EXIST_PRODUCT_ID)
        } catch (e: Exception) {
            assert(e is IllegalArgumentException)
            assert(e.message == "No stock for product $NOT_EXIST_PRODUCT_ID")
        }
    }

    @Test
    fun increaseProductStockTest() {
        productService.increaseStock(1)
        val searchStock = productService.searchStock(1)
        assert(searchStock >= DEFAULT_STOCK)
    }

    @Test
    fun decreaseProductStockTest() {
        productService.decreaseStock(1)
        val searchStock = productService.searchStock(1)
        assert(searchStock <= DEFAULT_STOCK)
    }

    @Test
    fun removeProductStockTest() {
        val id = 3L
        productService.removeStock(id)
        try {
            productService.searchStock(id)
        } catch (e: Exception) {
            assert(e is IllegalArgumentException)
            assert(e.message == "No stock for product $id")
        }
    }
}
