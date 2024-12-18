package kr.flab.f5.f5template.common.exception

sealed class BusinessException(
    open val code: ExceptionCode,
    override val message: String?,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)
