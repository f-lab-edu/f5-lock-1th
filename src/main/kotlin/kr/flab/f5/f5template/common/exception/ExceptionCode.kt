package kr.flab.f5.f5template.common.exception

enum class ExceptionCode(
    val cause: String
) {
    PRODUCT_NOT_FOUND("Not found product ")
}
