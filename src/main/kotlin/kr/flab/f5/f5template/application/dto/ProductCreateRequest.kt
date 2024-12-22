package kr.flab.f5.f5template.application.dto

data class ProductCreateRequest(
    val name: String,
    val price: Long,
    val stock: Long
)
