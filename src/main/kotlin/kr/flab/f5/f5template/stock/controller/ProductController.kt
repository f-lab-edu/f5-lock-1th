package kr.flab.f5.f5template.stock.controller

import io.swagger.annotations.Api
import kr.flab.f5.f5template.response.ApiResponse
import kr.flab.f5.f5template.stock.controller.dto.StockRequestDTO
import kr.flab.f5.f5template.stock.controller.dto.StockResponseDTO
import kr.flab.f5.f5template.stock.service.ProductService
import kr.flab.f5.f5template.stock.service.vo.StockVO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @PatchMapping("/stock/{id}/decreases")
    fun decreaseStock(
        @PathVariable id: Long
    ) {
        productService.decreaseStock(id)
    }

    @PatchMapping("/stock/{id}/increases")
    fun increaseStock(
        @PathVariable id: Long
    ) {
        productService.increaseStock(id)
    }

    @PostMapping("/stock")
    fun createStock(
        @RequestBody dto: StockRequestDTO): ResponseEntity<ApiResponse<Long>> {
        val stockVO = StockVO(dto.id, dto.value)

        productService.createStock(stockVO)
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(dto.id))
    }

    @GetMapping("/stock/{id}")
    fun readStock(
        @PathVariable id: Long): ResponseEntity<ApiResponse<StockResponseDTO>> {
        val stockVO = productService.readStock(id)

        val response = StockResponseDTO(stockVO.id, stockVO.value)
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response))
    }

    @PutMapping("/stock")
    fun updateStock(
        @RequestBody dto: StockRequestDTO): ResponseEntity<ApiResponse<Long>> {
        val stockVO = StockVO(dto.id, dto.value)

        productService.updateStock(stockVO)
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(stockVO.id))
    }

    @DeleteMapping("/{id}/stock")
    fun deleteStock(
        @PathVariable id: Long): ResponseEntity<ApiResponse<Nothing>> {
        productService.deleteStock(id)

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success())
    }
}
