package kr.flab.f5.f5template.stock.service

import kr.flab.f5.f5template.exception.stock.StockException
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import kr.flab.f5.f5template.stock.service.vo.StockVO
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
        val value = stock[id] ?: throw StockException.NotFountStockException(id)
        if (value <= 0) {
            throw StockException.NonDecreaseStockException(id)
        }
        stock[id] = value - 1
    }

    fun increaseStock(id: Long) {
        val value = stock[id] ?: throw StockException.NotFountStockException(id)
        stock[id] = value + 1
    }

    fun createStock(stockVO: StockVO) {
        if(stock.containsKey(stockVO.id)) {
            throw StockException.DuplicateStockException(stockVO.id)
        }
        stock[stockVO.id] = stockVO.value
    }

    fun readStock(id: Long): StockVO {
        val value = stock[id] ?: throw StockException.NotFountStockException(id)
        return StockVO(id, value)
    }

    fun updateStock(stockVO: StockVO) {
        if(!stock.containsKey(stockVO.id)) {
            throw StockException.NotFountStockException(stockVO.id)
        }
        stock[stockVO.id] = stockVO.value
    }

    fun deleteStock(id : Long) {
        if(stock.remove(id) == null) {
            throw StockException.NotFountStockException(id)
        }
    }
}
