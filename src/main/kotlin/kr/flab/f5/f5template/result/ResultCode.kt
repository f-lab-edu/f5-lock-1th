package kr.flab.f5.f5template.result

import org.springframework.http.HttpStatus

enum class ResultCode (
    internal val status: HttpStatus,
    internal val message: String
){
    //product 도메인
    PRODUCT_CREATE_SUCCESS(HttpStatus.CREATED, "상품이 정상적으로 등록되었습니다"),
    PRODUCT_FIND_SUCCESS(HttpStatus.OK, "상품 조회가 정상적으로 완료되었습니다"),
}