package kr.flab.f5.f5template.application

import io.swagger.annotations.Api
import kr.flab.f5.f5template.application.dto.ProductCreateRequest
import kr.flab.f5.f5template.application.dto.ProductUpdateRequest
import kr.flab.f5.f5template.result.ResultCode.*
import kr.flab.f5.f5template.result.ResultResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
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
        @PathVariable id: Long
    ) {
        productService.decreaseStock(id)
    }

    @PostMapping("/create")
    fun createProduct(@RequestBody productCreateRequest: ProductCreateRequest): ResponseEntity<ResultResponse> {
        productService.createStock(productCreateRequest)
        return ResponseEntity.ok(ResultResponse.of(PRODUCT_CREATE_SUCCESS))
    }

    @GetMapping()
    fun findAllProducts(): ResponseEntity<ResultResponse> {
        val findAllProducts = productService.findAllProducts()
        return ResponseEntity.ok(ResultResponse.of(PRODUCT_FIND_SUCCESS, findAllProducts))
    }

    @PatchMapping("/{productId}")
    fun updateProduct(
        @PathVariable productId: Long, // URL 경로에서 productId 추출
        @RequestBody updateRequest: ProductUpdateRequest // 요청 본문에서 업데이트 정보 추출
    ): ResponseEntity<ResultResponse> {
        productService.updateProduct(productId, updateRequest)
        return ResponseEntity.ok(ResultResponse.of(PRODUCT_UPDATE_SUCCESS))
    }

    @DeleteMapping("/delete/{deleteId}")
    fun deleteProduct(@PathVariable deleteId: Long): ResponseEntity<ResultResponse> {
        productService.deleteProduct(deleteId)
        return ResponseEntity.ok(ResultResponse.of(PRODUCT_DELETE_SUCCESS))
    }
}
