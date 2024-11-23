package kr.flab.f5.f5template.controller.response
import kr.flab.f5.f5template.exception.ErrorType

data class ApiResponse<T>(
    val message: String,
    val errorType: ErrorType? = null,
    val success: Boolean,
    val data: T? = null
)
