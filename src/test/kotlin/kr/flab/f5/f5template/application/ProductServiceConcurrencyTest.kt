package kr.flab.f5.f5template.application

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong
import kotlin.test.Test

@SpringBootTest
class ProductServiceConcurrencyTest {
    @Autowired
    private lateinit var productService: ProductService

    @Test
    fun `동시 상품 재고 등록 정합성 테스트`() {
        val executorService = Executors.newFixedThreadPool(100)
        val createCount = AtomicLong(0)

        repeat(1000) {
            executorService.submit {
                repeat(1000) {
                    productService.createStock(1)
                    createCount.incrementAndGet()
                }
            }
        }

        executorService.shutdown()

        while (!executorService.isTerminated) {
            Thread.sleep(100)
        }

        val previousProductStockSize = 3
        val expectedProductStockSize = previousProductStockSize + createCount.get()
        val countProductStockSize = AtomicLong(0)
        val expectedExceptionCount = AtomicLong(0)

        for (i in 1..expectedProductStockSize) {
            try {
                productService.searchStock(i)
                countProductStockSize.incrementAndGet()
            } catch (e: Exception) {
                if (e is IllegalArgumentException) {
                    println(e.message)
                    expectedExceptionCount.incrementAndGet()
                } else {
                    throw e
                }
            }
        }

        assert(countProductStockSize.get() == expectedProductStockSize)
        assert(expectedExceptionCount.get() == 0L)
    }

    @Test
    fun `단일 상품 입출고 정합성 테스트`() {
        val executorService = Executors.newFixedThreadPool(100)
        val decreaseCount = AtomicLong(0)
        val increaseCount = AtomicLong(0)

        repeat(1000) {
            executorService.submit {
                repeat(1000) {
                    val id = 1L
                    val random = (0..1).random()
                    if (random == 0) {
                        productService.increaseStock(id)
                        increaseCount.incrementAndGet()
                    } else {
                        productService.decreaseStock(id)
                        decreaseCount.incrementAndGet()
                    }
                }
            }
        }

        executorService.shutdown()

        while (!executorService.isTerminated) {
            Thread.sleep(100)
        }

        val expectedStock = DEFAULT_STOCK + increaseCount.get() - decreaseCount.get()
        val finalStock = productService.searchStock(1)

        assert(expectedStock == finalStock.toLong())
    }
}
