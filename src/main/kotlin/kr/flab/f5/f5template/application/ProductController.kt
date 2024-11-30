package kr.flab.f5.f5template.application

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kr.flab.f5.f5template.dto.CreateProductDto
import kr.flab.f5.f5template.dto.UpdateProductDto
import kr.flab.f5.f5template.mysql.jpa.entity.Product
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["Product"])
@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    @ApiOperation("Create a new product")
    fun createProduct(@RequestBody createProductDto: CreateProductDto): Product {
        return productService.createProduct(createProductDto)
    }

    @PutMapping("/{id}")
    @ApiOperation("Update an existing product")
    fun updateProduct(@PathVariable id: Long, @RequestBody updateProductDto: UpdateProductDto): Product {
        return productService.updateProduct(id, updateProductDto)
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a product")
    fun deleteProduct(@PathVariable id: Long) {
        productService.deleteProduct(id)
    }

    @GetMapping("/{id}")
    @ApiOperation("Get a product by ID")
    fun getProduct(@PathVariable id: Long): Product {
        return productService.getProduct(id)
    }

    @PatchMapping("/{id}/stock")
    fun decreaseStock(
        @PathVariable id: Long
    ) {
        productService.decreaseStock(id)
    }
}
