package kr.flab.f5.f5template.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    internal val status: HttpStatus,
    internal val message: String
) {
    PRODUCT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 상품입니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다"),
}