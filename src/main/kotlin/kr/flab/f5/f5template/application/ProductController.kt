package kr.flab.f5.f5template.application

import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["Product"])
@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService,
) : ProductControllerSpecification {
    @PostMapping
    override fun createProduct(stock: Int) {
        productService.createProduct(stock)
    }

    @GetMapping("/{id}/stock")
    override fun searchStock(
        @PathVariable id: Long,
    ): ProductStockResponse =
        ProductStockResponse(
            id = id,
            stock = productService.searchStock(id),
        )

    @PostMapping("/{id}/stock/increment")
    override fun increaseStock(
        @PathVariable id: Long,
    ) {
        productService.increaseStock(id)
    }

    @PostMapping("/{id}/stock/decrement")
    override fun decreaseStock(
        @PathVariable id: Long,
    ) {
        productService.decreaseStock(id)
    }

    @DeleteMapping("/{id}")
    override fun removeProduct(
        @PathVariable id: Long,
    ) {
        productService.removeProduct(id)
    }
}

data class ProductStockResponse(
    val id: Long,
    val stock: Int,
)
