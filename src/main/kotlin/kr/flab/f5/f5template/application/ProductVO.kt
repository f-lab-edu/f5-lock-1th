package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.mysql.jpa.entity.Product

class ProductVO(
    var id: Long? = 0L,
    val name: String,
    val price: Long,
    val stock: Long
) {
    fun toProduct() = Product(
        name = name,
        price = price,
        stock = stock
    )

    companion object {
        fun from(product: Product) = ProductVO(
            id = product.id,
            name = product.name,
            price = product.price,
            stock = product.stock
        )
    }
}
