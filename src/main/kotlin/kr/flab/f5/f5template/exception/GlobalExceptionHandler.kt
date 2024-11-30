package kr.flab.f5.f5template.exception

import kr.flab.f5.f5template.exception.stock.StockException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(StockException::class)
    fun handleStockException(e: StockException): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(status = e.status.value(), error = e.status.reasonPhrase, message = e.message)
        return ResponseEntity(exceptionResponse, e.status)
    }
}