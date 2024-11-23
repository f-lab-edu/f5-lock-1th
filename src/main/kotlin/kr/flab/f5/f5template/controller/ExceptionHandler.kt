package kr.flab.f5.f5template.controller

import kr.flab.f5.f5template.controller.response.ApiResponse
import kr.flab.f5.f5template.exception.ApiException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ApiException::class)
    protected fun handleApiException(e: ApiException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity
            .status(e.httpStatus)
            .body(e.message?.let { ApiResponse(message = it, errorType = e.errorType, success = false, data = null) })
    }
}
