package kr.flab.f5.f5template.application

import io.swagger.annotations.Api
import kr.flab.f5.f5template.result.ResultCode.*
import kr.flab.f5.f5template.result.ResultResponse
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
        @PathVariable id: Long
    ) {
        productService.decreaseStock(id)
    }

    @PostMapping("/create")
    fun createProduct(@RequestBody productCreateRequest: ProductCreateRequest): ResponseEntity<ResultResponse> {
        productService.createStock(productCreateRequest)
        return ResponseEntity.ok(ResultResponse.of(PRODUCT_CREATE_SUCCESS))
    }
}
