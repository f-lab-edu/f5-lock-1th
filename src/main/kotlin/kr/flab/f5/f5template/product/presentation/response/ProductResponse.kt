package kr.flab.f5.f5template.product.presentation.response

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: Long,
    val stock: Long
)