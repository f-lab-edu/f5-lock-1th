package kr.flab.f5.f5template.product.presentation

import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import kr.flab.f5.f5template.product.application.ProductService
import kr.flab.f5.f5template.product.presentation.request.ProductRequest
import kr.flab.f5.f5template.product.presentation.response.ProductResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Api(tags = ["Product"])
@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "상품 목록 조회")
    fun findProducts(): List<ProductResponse> {
        return productService.findProducts()
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "상품 조회")
    fun findProductById(@PathVariable id: Long): ProductResponse {
        return productService.findProductById(id)
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "상품 등록")
    fun createProduct(@RequestBody @Valid request: ProductRequest) {
        productService.createProduct(request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "상품 삭제")
    fun deleteProduct(@PathVariable id: Long) {
        productService.deleteProduct(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "상품 수정")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody @Valid request: ProductRequest
    ) {
        productService.updateProduct(id, request)
    }

    @PatchMapping("/{id}/stock")
    fun decreaseStock(
        @PathVariable id: Long
    ) {
        productService.decreaseStock(id)
    }
}
