package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.dto.CreateProductDto
import kr.flab.f5.f5template.dto.UpdateProductDto
import kr.flab.f5.f5template.mysql.jpa.entity.Product
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    @Transactional
    fun createProduct(createProductDto: CreateProductDto): Product {
        val product = Product(
            name = createProductDto.name,
            price = createProductDto.price,
            stock = createProductDto.quantity
        )
        return productRepository.save(product)
    }

    @Transactional
    fun updateProduct(id: Long, updateProductDto: UpdateProductDto): Product {
        val product = productRepository.findById(id).orElseThrow { IllegalArgumentException("No product for id $id") }
        product.updateName(updateProductDto.name)
        product.updatePrice(updateProductDto.price)
        product.updateStock(updateProductDto.quantity)
        return productRepository.save(product)
    }

    fun deleteProduct(id: Long) {
        productRepository.deleteById(id)
    }

    fun getProduct(id: Long): Product {
        return productRepository.findById(id).orElseThrow { IllegalArgumentException("No product for id $id") }
    }

    fun decreaseStock(id: Long) {
        val currentStock = stock[id] ?: throw IllegalArgumentException("No stock for product $id")
        if (currentStock <= 0) {
            throw IllegalArgumentException("No stock for product $id")
        }
        stock[id] = currentStock - 1
    }
}
