package kr.flab.f5.f5template.common

import kr.flab.f5.f5template.common.exception.BusinessException
import kr.flab.f5.f5template.common.exception.ExceptionResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ProductAdvice {
    @ExceptionHandler(BusinessException::class)
    @ResponseStatus(HttpStatus.OK)
    fun businessExceptionHandler(e: BusinessException): ExceptionResponseDTO<String> {
        return ExceptionResponseDTO(code = e.code, content = e.message)
    }
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.OK)
    fun exceptionHandler(e: Exception): ExceptionResponseDTO<String> {
        return ExceptionResponseDTO(content = e.message)
    }
}
