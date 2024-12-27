package kr.flab.f5.f5template.exception
import org.springframework.http.HttpStatus

class ApiException(
    val errorMessage: String,
    val errorType: ErrorType,
    val httpStatus: HttpStatus
) : RuntimeException(errorMessage)
