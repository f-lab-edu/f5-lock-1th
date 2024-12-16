package kr.flab.f5.f5template.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Product", description = "상품 도메인 API")
interface ProductControllerSpecification {
    @Operation(
        summary = "상품 재고 생성 API",
        description = "id에 해당하는 상품의 재고를 1로 설정합니다.",
    )
    @ApiResponse(responseCode = "200", description = "상품 생성 성공")
    fun createProduct(stock: Int)

    @Operation(
        summary = "상품 재고 검색 API",
        description = "id에 해당하는 상품의 재고를 조회합니다.",
    )
    @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    fun searchStock(id: Long): ProductStockResponse

    @Operation(
        summary = "상품 입고 API",
        description = "id에 해당하는 상품의 재고를 1 증가시킵니다.",
    )
    @ApiResponse(responseCode = "200", description = "상품 입고 성공")
    fun increaseStock(id: Long)

    @Operation(
        summary = "상품 출고 API",
        description = "id에 해당하는 상품의 재고를 1 감소시킵니다.",
    )
    @ApiResponse(responseCode = "200", description = "상품 출고 성공")
    fun decreaseStock(id: Long)

    @Operation(
        summary = "상품 출고 API",
        description = "id에 해당하는 상품의 재고를 삭제합니다.",
    )
    @ApiResponse(responseCode = "200", description = "상품 삭제 성공")
    fun removeProduct(id: Long)
}
