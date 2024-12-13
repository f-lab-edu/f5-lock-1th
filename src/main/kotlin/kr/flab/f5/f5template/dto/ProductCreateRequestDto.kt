package kr.flab.f5.f5template.dto

/*
    data 키워드로 dto를 만들게되면, 소괄호에 넣은 값들에 한해서
    구조분해할당, equals, hashCode, toString, copy등 여러 메소드들을 지원합니다.
 */
data class ProductCreateRequestDto(
    val name: String,
    val price: Long,
    val stock: Long
)
