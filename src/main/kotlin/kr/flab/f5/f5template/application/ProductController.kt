package kr.flab.f5.f5template.application

import io.swagger.annotations.Api
import kr.flab.f5.f5template.common.ResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["Product"])
@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {

    @PatchMapping("/{id}/stock")
    fun decreaseStock(
        @PathVariable id: Long
    ) {
        productService.decreaseStock(id)
    }

    @GetMapping("/{id}")
    fun findProduct(
        @PathVariable id: Long
    ): ResponseDTO<ProductDTO> {
        return ResponseDTO.success(ProductDTO.from(productService.findProduct(id)))
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody product: ProductDTO
    ) {
        productService.updateProduct(id, product.toVO())
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(
        @RequestBody product: ProductDTO
    ) {
        productService.createProduct(product.toVO())
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(
        @PathVariable id: Long
    ) {
        productService.deleteProduct(id)
    }
}
