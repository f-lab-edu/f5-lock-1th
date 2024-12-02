package kr.flab.f5.f5template.application.dto

import kr.flab.f5.f5template.mysql.jpa.entity.Product

class ProductDto(
    val id: Long,
    val name: String,
    val price: Long,
    val stock: Long
) {
    companion object {
        fun fromEntity(entity: Product): ProductDto {
            return ProductDto(
                entity.id,
                entity.name,
                entity.price,
                entity.stock
            )
        }
    }
}