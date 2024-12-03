package kr.flab.f5.f5template.dto

data class ProductUpdateRequestDto(
    val name: String,
    val price: Long,
    val stockChange: Long
)
