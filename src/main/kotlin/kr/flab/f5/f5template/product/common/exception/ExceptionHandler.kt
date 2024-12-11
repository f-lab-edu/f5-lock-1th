package kr.flab.f5.f5template.product.common.exception

import mu.KotlinLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(Exception::class)
    fun handleEmptyResultDataAccessException(ex: Exception): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            code = ExceptionCode.NOT_FOUND.code,
            message = ExceptionCode.NOT_FOUND.message
        )

        logger.warn(ex) { "[Exception] CODE: ${response.code}" }

        return ResponseEntity
            .status(ExceptionCode.NOT_FOUND.status)
            .body(response)
    }

}