package kr.flab.f5.f5template.common.exception

data class ExceptionResponseDTO<T>(
    val code: ExceptionCode? = null,
    val content: T?
)
