package kr.flab.f5.f5template.common

data class ResponseDTO<T>(
    val content: T? = null
) {
    companion object {
        fun <T> success(content: T? = null): ResponseDTO<T> {
            return ResponseDTO(content)
        }
    }
}
