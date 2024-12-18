package kr.flab.f5.f5template.exception

import kr.flab.f5.f5template.exception.stock.StockException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    private fun toList(bindingResult: BindingResult): List<ValidationFieldException> {
        return bindingResult.fieldErrors.map { error ->
            ValidationFieldException(field = error.field, value = error.rejectedValue?.toString(), message = error.defaultMessage ?: "잘못된 값 입력")
        }
    }

    @ExceptionHandler(StockException::class)
    fun handleStockException(e: StockException): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(status = e.status.value(), error = e.status.reasonPhrase, message = e.message)
        return ResponseEntity(exceptionResponse, e.status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ExceptionResponseWithField> {
        val fieldExceptions = toList(e.bindingResult)
        val exceptionResponse = ExceptionResponseWithField(error = HttpStatus.BAD_REQUEST.reasonPhrase, message = "유효성 검사 실패", fields = fieldExceptions)
        return ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST)
    }
}