package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.dto.CreateProductDto
import kr.flab.f5.f5template.dto.UpdateProductDto
import kr.flab.f5.f5template.mysql.jpa.entity.Product
import kr.flab.f5.f5template.mysql.jpa.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.util.*

class ProductServiceTest {

    private val productRepository: ProductRepository = mock(ProductRepository::class.java)
    private val productService = ProductService(productRepository)

    @Test
    fun createProduct_createsAndReturnsProduct() {
        val createProductDto = CreateProductDto("New Product", 1000, 10)
        val product = Product(1, "New Product", 1000, 10)
        `when`(productRepository.save(any(Product::class.java))).thenReturn(product)

        val result = productService.createProduct(createProductDto)

        assertEquals(product, result)
        verify(productRepository, times(1)).save(any(Product::class.java))
    }

    @Test
    fun updateProduct_updatesAndReturnsProduct() {
        val updateProductDto = UpdateProductDto("Updated Product", 2000, 20)
        val product = Product(1, "Old Product", 1000, 10)
        `when`(productRepository.findById(1)).thenReturn(Optional.of(product))
        `when`(productRepository.save(any(Product::class.java))).thenReturn(product)

        val result = productService.updateProduct(1, updateProductDto)

        assertEquals(product, result)
        verify(productRepository, times(1)).findById(1)
        verify(productRepository, times(1)).save(any(Product::class.java))
    }

    @Test
    fun deleteProduct_deletesProduct() {
        doNothing().`when`(productRepository).deleteById(1)

        productService.deleteProduct(1)

        verify(productRepository, times(1)).deleteById(1)
    }

    @Test
    fun getProduct_returnsProduct() {
        val product = Product(1, "Sample Product", 1000, 10)
        `when`(productRepository.findById(1)).thenReturn(Optional.of(product))

        val result = productService.getProduct(1)

        assertEquals(product, result)
        verify(productRepository, times(1)).findById(1)
    }

    @Test
    fun getProduct_throwsExceptionWhenProductNotFound() {
        `when`(productRepository.findById(1)).thenReturn(Optional.empty())

        assertThrows<IllegalArgumentException> { productService.getProduct(1) }
        verify(productRepository, times(1)).findById(1)
    }
}
