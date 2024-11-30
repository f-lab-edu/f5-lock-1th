package kr.flab.f5.f5template.stock.controller

import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
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
import javax.validation.Valid

@Api(tags = ["Product"])
@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {

    @Operation(
        summary = "[상품] - 재고 감소 API",
        description = "지정된 아이디에 대한 상품의 재고를 하나 감소 시킵니다."
    )
    @PatchMapping("/stock/{id}/decreases")
    fun decreaseStock(
        @PathVariable id: Long
    ) {
        productService.decreaseStock(id)
    }

    @Operation(
        summary = "[상품] - 재고 증가 API",
        description = "지정된 아이디에 대한 상품의 재고를 하나 증가 시킵니다."
    )
    @PatchMapping("/stock/{id}/increases")
    fun increaseStock(
        @PathVariable id: Long
    ) {
        productService.increaseStock(id)
    }

    @Operation(
        summary = "[상품] - 상품 등록 API",
        description = "지정된 아이디와 재고 수 만큼으로 상품을 등록합니다."
    )
    @PostMapping("/stock")
    fun createStock(
        @Valid @RequestBody dto: StockRequestDTO): ResponseEntity<ApiResponse<Long>> {
        val stockVO = StockVO(dto.id, dto.value)

        productService.createStock(stockVO)
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(dto.id))
    }

    @Operation(
        summary = "[상품] - 상품 조회 API",
        description = "지정된 아이디의 상품의 대한 정보를 조회합니다."
    )
    @GetMapping("/stock/{id}")
    fun readStock(
        @PathVariable id: Long): ResponseEntity<ApiResponse<StockResponseDTO>> {
        val stockVO = productService.readStock(id)

        val response = StockResponseDTO(stockVO.id, stockVO.value)
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response))
    }

    @Operation(
        summary = "[상품] - 상품 수정 API",
        description = "지정된 아이디의 상품에 대한 재고를 입력된 값으로 수정합니다."
    )
    @PutMapping("/stock")
    fun updateStock(
        @Valid @RequestBody dto: StockRequestDTO): ResponseEntity<ApiResponse<Long>> {
        val stockVO = StockVO(dto.id, dto.value)

        productService.updateStock(stockVO)
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(stockVO.id))
    }

    @Operation(
        summary = "[상품] - 상품 삭제 API",
        description = "지정된 아이디의 상품을 삭제합니다."
    )
    @DeleteMapping("/{id}/stock")
    fun deleteStock(
        @PathVariable id: Long): ResponseEntity<ApiResponse<Nothing>> {
        productService.deleteStock(id)

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success())
    }
}
