package kr.flab.f5.f5template.error

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(BaseException::class)
    protected fun handleBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(e.errorCode.status, e.errorCode.message)
        return ResponseEntity.status(e.errorCode.status).body(errorResponse)
    }
}