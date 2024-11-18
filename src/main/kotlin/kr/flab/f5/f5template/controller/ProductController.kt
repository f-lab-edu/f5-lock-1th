package kr.flab.f5.f5template.controller

import io.swagger.annotations.Api
import kr.flab.f5.f5template.controller.request.DecreaseStockRequest
import kr.flab.f5.f5template.service.ProductService
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
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
}
