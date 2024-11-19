package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.dto.ProductCreateRequestDto
import kr.flab.f5.f5template.dto.ProductReadResponseDto
import kr.flab.f5.f5template.dto.ProductUpdateRequestDto
import kr.flab.f5.f5template.mysql.jpa.entity.Product
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    // 데이터베이스 없이 순수 자바로 동시성 이슈를 다뤄보기 위해 아직은 데이터베이스를 사용하지 않습니다.
    private val productRepository: ProductRepository,
) : ProductService {

    private val products = HashMap<Long, Product>()

    init {
        products[1L] = Product(1, "test1", 10000, 10000)
        products[2L] = Product(2, "test2", 20000, 20000)
        products[3L] = Product(3, "test3", 30000, 30000)
    }

    override fun decreaseStock(id: Long) {
        val product = products[id] ?: throw IllegalArgumentException("No product numbered by $id")
        val currentStock = product.stock ?: throw IllegalArgumentException("No stock for productId : $id ")
        if (currentStock <= 0) {
            throw IllegalArgumentException("No stock for product $id")
        }
        product.decreaseStock()
    }

    override fun increaseStock(id: Long) {
        val product = products[id] ?: throw IllegalArgumentException("No stock for product $id")
        product.increaseStock()
    }

    override fun create(createDto: ProductCreateRequestDto): Product {
        val size = products.size.toLong()
        val newProduct = Product(size, createDto.name, createDto.price, createDto.stock)

        products[size] = newProduct
        return products[size] ?: throw RuntimeException("Product create failed")
    }

    override fun read(id: Long) : Product{
        val product = products[id] ?: throw IllegalArgumentException("No product numbered by $id")
        return product
    }

    override fun update(id: Long, updateDto: ProductUpdateRequestDto) : Product{
        val product = read(id)
        product.name = updateDto.name
        product.price = updateDto.price
        product.updateStock(updateDto.stockChange)
        return product
    }

    override fun delete(id: Long) {
        products.remove(id)
    }
}
