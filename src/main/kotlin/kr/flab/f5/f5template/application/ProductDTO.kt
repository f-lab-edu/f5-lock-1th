package kr.flab.f5.f5template.application

data class ProductDTO(
    var id: Long? = 0L,
    val name: String,
    val price: Long,
    val stock: Long
) {
    fun toVO() = ProductVO(
        name = name,
        price = price,
        stock = stock
    )

    companion object {
        fun from(product: ProductVO) = ProductDTO(
            id = product.id,
            name = product.name,
            price = product.price,
            stock = product.stock
        )
    }
}
