package kr.flab.f5.f5template.stock.controller.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

data class StockRequestDTO(
    @field:NotNull(message = "상품의 ID는 필수 입력 값 입니다.")
    val id: Long,
    @field:PositiveOrZero(message = "재고의 수량은 0 이상이여야 합니다.")
    val value: Int
)