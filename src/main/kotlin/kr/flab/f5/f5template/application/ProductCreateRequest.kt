package kr.flab.f5.f5template.application

data class ProductCreateRequest(
    val name: String,
    val price: Long,
    val stock: Long
)
