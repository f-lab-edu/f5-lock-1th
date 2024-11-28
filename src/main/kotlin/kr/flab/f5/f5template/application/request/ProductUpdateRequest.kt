package kr.flab.f5.f5template.application.request

class ProductUpdateRequest(
    val id: Long,
    val name: String,
    val price: Long,
    val stock: Long
)