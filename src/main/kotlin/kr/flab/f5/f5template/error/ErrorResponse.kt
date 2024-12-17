package kr.flab.f5.f5template.error

import org.springframework.http.HttpStatus

class ErrorResponse(
    val status: HttpStatus, val message: String
)