package kr.flab.f5.f5template.controller

import io.swagger.annotations.Api
import kr.flab.f5.f5template.controller.request.CreateProductRequest
import kr.flab.f5.f5template.controller.request.DecreaseStockRequest
import kr.flab.f5.f5template.controller.response.ApiResponse
import kr.flab.f5.f5template.controller.response.ProductResult
import kr.flab.f5.f5template.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["Product"])
@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {

    @PatchMapping("/{id}/stock")
    fun decreaseStock(
        @PathVariable id: Long,
        @RequestBody request: DecreaseStockRequest
    ) {
        productService.decreaseStock(id)
    }

    @PostMapping
    fun addProduct(@RequestBody request: CreateProductRequest): ResponseEntity<ApiResponse<ProductResult>> {
        val productResult = productService.createProduct(request)
        return ResponseEntity.ok().body(ApiResponse("상품 생성 성공", success = true, data = productResult))
    }
}
