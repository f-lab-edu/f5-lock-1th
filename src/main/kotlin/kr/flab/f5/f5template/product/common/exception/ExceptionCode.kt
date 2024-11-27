package kr.flab.f5.f5template.product.common.exception

import org.springframework.http.HttpStatus

enum class ExceptionCode(
    val status: HttpStatus,
    val code: Int,
    val message: String
) {
    NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, 1000, "대상을 찾을 수 없습니다.");
}