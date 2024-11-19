package kr.flab.f5.f5template.dto

import java.time.Instant

data class ProductReadResponseDto (
    val id: Long,
    val name: String,
    val price: Long,
    val stock: Long,
    val createdAt: Instant,
    val updatedAt: Instant
)
