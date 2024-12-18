package kr.flab.f5.f5template.response

import java.time.LocalDateTime

data class ApiResponse<T> (
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val data: T? = null
) {
    companion object{
        fun <T> success(data : T? = null): ApiResponse<T> {
            return ApiResponse(data = data)
        }

        fun success(): ApiResponse<Nothing> {
            return ApiResponse()
        }
    }
}