package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.dto.ProductCreateRequestDto
import kr.flab.f5.f5template.dto.ProductUpdateRequestDto
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ProductServiceHashMapImplTest {
    @Mock
    private lateinit var productRepository: ProductRepository

    private lateinit var productService: ProductServiceHashMapImpl

    // HashMap과 서비스 내에서 Map을 초기화하고 있기 때문에, Mock 사용 불가
    @BeforeEach
    fun init() {
        productService = ProductServiceHashMapImpl(productRepository)
    }

    // 서비스 내 Map을 사용 중이라 직접 조회 방법이 없기 때문에, 테스트끼리 의존성이 생기더라도 read를 이용해 값 검증
    @Test
    fun decreaseStock를_이용해_재고를_감소시킬_수_있다() {
        // given
        val targetId: Long = 1
        val beforeStock = productService.read(targetId).stock

        // when
        productService.decreaseStock(targetId)
        val afterProduct = productService.read(targetId)

        // then
        assertThat(afterProduct.stock).isEqualTo(beforeStock - 1)
    }

    @Test
    fun increaseStock을_이용해_재고를_증가시킬_수_있다() {
        // given
        val targetId: Long = 1
        val beforeStock = productService.read(targetId).stock

        // when
        productService.increaseStock(targetId)
        val afterProduct = productService.read(targetId)

        // then
        assertThat(afterProduct.stock).isEqualTo(beforeStock + 1)
    }

    @Test
    fun create를_이용해_Product를_생성할_수_있다() {
        // given
        val name = "test4"
        val price = 999L
        val stock = 100L
        val createDto: ProductCreateRequestDto = ProductCreateRequestDto(name = name, price = price, stock = stock)

        // when
        val createdProduct = productService.create(createDto)

        // then
        assertThat(createdProduct.stock).isEqualTo(stock)
        assertThat(createdProduct.name).isEqualTo(name)
        assertThat(createdProduct.price).isEqualTo(price)
    }

    // service에 init 되어있는 객체를 조회하는 것으로 대체
    @Test
    fun read를_이용햬_Product를_조회할_수_있다() {
        // given
        val targetId = 1L

        // when
        // then
        assertThatCode {
            productService.read(targetId)
        }.doesNotThrowAnyException()
    }

    @Test
    fun update를_이용해_Product를_수정할_수_있다() {
        // given
        val targetId = 1L
        val name = "modified"
        val price = 9487L
        val stockChange = 10L
        val updateDto = ProductUpdateRequestDto(name = name, price = price, stockChange = stockChange)
        val beforeProduct = productService.read(targetId)

        val beforeStock = beforeProduct.stock

        // when
        val updatedProduct = productService.update(targetId, updateDto)

        // then
        assertThat(updatedProduct.stock).isEqualTo(beforeStock + stockChange)
        assertThat(updatedProduct.name).isEqualTo(name)
        assertThat(updatedProduct.price).isEqualTo(price)
    }

    @Test
    fun delete를_이용해_Product를_삭제할_수_있다() {
        // given
        val targetId = 1L

        // when
        // then
        assertThatCode {
            productService.delete(targetId)
        }.doesNotThrowAnyException()
    }
}