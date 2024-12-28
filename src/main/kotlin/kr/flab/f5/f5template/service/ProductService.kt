package kr.flab.f5.f5template.service

import kr.flab.f5.f5template.controller.request.SetProductRequest
import kr.flab.f5.f5template.controller.response.ProductResult
import kr.flab.f5.f5template.controller.response.StockResult
import kr.flab.f5.f5template.exception.ApiException
import kr.flab.f5.f5template.exception.ErrorType
import kr.flab.f5.f5template.mysql.jpa.entity.Product
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    // 데이터베이스 없이 순수 자바로 동시성 이슈를 다뤄보기 위해 아직은 데이터베이스를 사용하지 않습니다.
    private val productRepository: ProductRepository
) {

    fun decreaseStock(id: Long,quantity: Long) : StockResult {
        val product = productRepository.findByIdWithLock(id)
        product?.let {
            try{
                product.decreaseStock(quantity)
                return StockResult(true,"재고 차감 성공")
            }
            catch (e : Exception) {
                return StockResult(false,e.message);
            }

        } ?: run {
            return StockResult(false,"상품정보 조회 실패")
        }
    }

    @Transactional
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

    @Transactional(readOnly = true)
    fun getProduct(id: Long): ProductResult {
        return productRepository.findById(id)
            .orElseThrow { ApiException("상품 조회 실패", ErrorType.NO_RESOURCE, HttpStatus.OK) }
            .run {
                ProductResult(this.id, this.name, this.price, this.stock)
            }
    }

    @Transactional
    fun reviseProduct(id: Long, request: SetProductRequest): ProductResult {
        val product = productRepository.findById(id)
            .orElseThrow { ApiException("상품 수정 실패", ErrorType.NO_RESOURCE, HttpStatus.OK) }
            .run {
              this
            }
        val remainStock = product?.modifyStock(request.stock)
        return ProductResult(product.id,product.name,product.price,remainStock)
    }

    @Transactional
    fun deleteProduct(id: Long): ProductResult {
        return productRepository.findById(id)
            .orElseThrow { ApiException("상품 삭제 실패", ErrorType.NO_RESOURCE, HttpStatus.OK) }
            .run {
                productRepository.deleteById(id)
                ProductResult(this.id, this.name)
            }
    }
}
