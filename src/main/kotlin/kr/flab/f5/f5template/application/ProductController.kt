package kr.flab.f5.f5template.application

import io.swagger.annotations.Api
import kr.flab.f5.f5template.dto.ProductCreateRequestDto
import kr.flab.f5.f5template.dto.ProductUpdateRequestDto
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
    @PostMapping("/create")
    fun create(
        @RequestBody createDto: ProductCreateRequestDto
    ) {
        productService.create(createDto)
    }

    @GetMapping("/{id}")
    fun read(
        @PathVariable id: Long
    ) {
        productService.read(id)
    }

    @PatchMapping("/{id}/update")
    fun update(
        @PathVariable id: Long,
        @RequestBody updateDto: ProductUpdateRequestDto
    ) {
        productService.update(id, updateDto)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        productService.delete(id)
    }

    @PatchMapping("/{id}/stock")
    fun decreaseStock(
        @PathVariable id: Long
    ) {
        productService.decreaseStock(id)
    }
}
