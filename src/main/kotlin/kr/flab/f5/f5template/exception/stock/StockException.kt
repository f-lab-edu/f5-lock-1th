package kr.flab.f5.f5template.exception.stock

import org.springframework.http.HttpStatus

sealed class StockException(
    override val message: String,
    open val status: HttpStatus
) : RuntimeException(message) {
    class DuplicateStockException(id: Long) : StockException("ID 가 ${id}인 STOCK 이미 존재 합니다.", HttpStatus.CONFLICT)
    class NotFountStockException(id: Long) : StockException("ID 가 ${id}인 STOCK 존재하지 않습니다.", HttpStatus.NOT_FOUND)
    class NonDecreaseStockException(id: Long) : StockException("ID 가 ${id}인 STOCK 은 더 감소 시킬 수 없습니다.", HttpStatus.BAD_REQUEST)
}