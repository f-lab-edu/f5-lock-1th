package kr.flab.f5.f5template.dto

import java.time.Instant

data class ProductUpdateRequestDto (
    val name: String,
    val price: Long,
    val stockChange: Long
)