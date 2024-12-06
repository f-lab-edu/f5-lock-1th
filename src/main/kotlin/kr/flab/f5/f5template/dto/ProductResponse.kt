package kr.flab.f5.f5template.dto

import java.time.LocalDateTime

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: Int,
    val quantity: Int,
    val createdAt: LocalDateTime,
)
