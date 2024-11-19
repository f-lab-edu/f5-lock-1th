package kr.flab.f5.f5template.controller.response

data class ApiResponse<T>(
    val message: String,
    val success: Boolean,
    val data: T? = null
)
