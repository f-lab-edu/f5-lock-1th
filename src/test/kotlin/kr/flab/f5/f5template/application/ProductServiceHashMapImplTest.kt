package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.dto.ProductCreateRequestDto
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.aspectj.lang.annotation.Before
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ProductServiceImplTest {
    private lateinit var productRepository: ProductRepository
    private lateinit var productService: ProductServiceImpl

    // HashMap과 서비스 내에서 Map을 초기화하고 있기 때문에, Mock 사용 불가
    @BeforeEach
    fun init() {
        productService = ProductServiceImpl(productRepository)
    }

    // 서비스 내 Map을 사용 중이라 직접 조회 방법이 없기 때문에, 테스트끼리 의존성이 생기더라도 read를 이용해 값 검증
    @Test
    fun decreaseStock를_이용해_재고를_감소시킬_수_있다() {
        // given
        val target: Long = 1
        val beforeProduct = productService.read(target)

        // when
        productService.decreaseStock(target)
        val afterProduct = productService.read(target)

        // then
        assertThat(afterProduct.stock).isEqualTo(beforeProduct.stock - 1)
    }

    @Test
    fun increaseStock을_이용해_재고를_증가시킬_수_있다() {
        // given
        val target: Long = 1
        val beforeProduct = productService.read(target)

        // when
        productService.increaseStock(target)
        val afterProduct = productService.read(target)

        // then
        assertThat(afterProduct.stock).isEqualTo(beforeProduct.stock + 1)
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

    // Service에 init 되어있는 객체 조회하는 것으로 대체
    @Test
    fun read를_이용햬_Product를_조회할_수_있다() {
        // given
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }
}