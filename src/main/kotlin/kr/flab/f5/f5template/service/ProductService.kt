package kr.flab.f5.f5template.service

import kr.flab.f5.f5template.controller.request.SetProductRequest
import kr.flab.f5.f5template.controller.response.ProductResult
import kr.flab.f5.f5template.exception.ApiException
import kr.flab.f5.f5template.exception.ErrorType
import kr.flab.f5.f5template.mysql.jpa.entity.Product
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ProductService(
    // 데이터베이스 없이 순수 자바로 동시성 이슈를 다뤄보기 위해 아직은 데이터베이스를 사용하지 않습니다.
    private val productRepository: ProductRepository,
) {

    private val stock = HashMap<Long, Int>()
    private val objectList = mutableListOf<ByteArray>()

    init {
        stock[1] = 1000000
        stock[2] = 1000000
        stock[3] = 1000000
    }

    fun decreaseStock(id: Long) {
        val currentStock = stock[id] ?: throw IllegalArgumentException("No stock for product $id")
        if (currentStock <= 0) {
            throw IllegalArgumentException("No stock for product $id")
        }
        stock[id] = currentStock - 1
    }

    fun createProduct(request: SetProductRequest): ProductResult {
        val product = Product(
            name = request.name,
            price = request.price,
            stock = request.stock
        )
        productRepository.save(product)
        return ProductResult(
            id = product.id,
            name = product.name
        )
    }

    fun getProduct(id: Long): ProductResult {
        repeat(100) {
            objectList.add(ByteArray(10485)) // 큰 객체 생성
        }
        return productRepository.findById(id)
            .orElseThrow { ApiException("상품 조회 실패", ErrorType.NO_RESOURCE, HttpStatus.OK) }
            .run {
                ProductResult(this.id, this.name, this.price, this.stock)
            }
    }

    fun reviseProduct(id: Long, request: SetProductRequest): ProductResult {
        return productRepository.findById(id)
            .orElseThrow { ApiException("상품 수정 실패", ErrorType.NO_RESOURCE, HttpStatus.OK) }
            .run {
                this.reviseProduct(request.name, request.price, request.stock)
                productRepository.save(this)
                ProductResult(this.id, this.name)
            }
    }

    fun deleteProduct(id: Long): ProductResult {
        return productRepository.findById(id)
            .orElseThrow { ApiException("상품 삭제 실패", ErrorType.NO_RESOURCE, HttpStatus.OK) }
            .run {
                productRepository.deleteById(id)
                ProductResult(this.id, this.name)
            }
    }
}
