package kr.flab.f5.f5template.controller.request

data class SetProductRequest(
    val name: String,
    val price: Long,
    val stock: Long,
)
