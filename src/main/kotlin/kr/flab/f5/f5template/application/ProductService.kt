package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

const val DEFAULT_STOCK = 1000000

@Service
class ProductService(
    // 데이터베이스 없이 순수 자바로 동시성 이슈를 다뤄보기 위해 아직은 데이터베이스를 사용하지 않습니다.
    private val productRepository: ProductRepository,
) {
    private val stock = ConcurrentHashMap<Long, Int>()
    private lateinit var id: AtomicLong

    init {
        stock[1] = DEFAULT_STOCK
        stock[2] = DEFAULT_STOCK
        stock[3] = DEFAULT_STOCK
        id = AtomicLong(stock.size.toLong())
    }

    fun createProduct(quantity: Int) {
        stock[id.incrementAndGet()] = quantity
    }

    @Synchronized
    fun decreaseStock(id: Long) {
        val currentStock = stock[id] ?: throw IllegalArgumentException("No stock for product $id")
        if (currentStock <= 0) {
            throw IllegalArgumentException("No stock for product $id")
        }
        stock[id] = currentStock - 1
    }

    @Synchronized
    fun increaseStock(id: Long) {
        val currentStock = stock[id] ?: throw IllegalArgumentException("No stock for product $id")
        if (currentStock <= 0) {
            throw IllegalArgumentException("No stock for product $id")
        }
        stock[id] = currentStock + 1
    }

    fun searchStock(id: Long): Int = stock[id] ?: throw IllegalArgumentException("No stock for product $id")

    fun removeProduct(id: Long) {
        val currentStock = stock[id] ?: throw IllegalArgumentException("No stock for product $id")
        if (currentStock <= 0) {
            throw IllegalArgumentException("No stock for product $id")
        }
        stock.remove(id)
    }
}
