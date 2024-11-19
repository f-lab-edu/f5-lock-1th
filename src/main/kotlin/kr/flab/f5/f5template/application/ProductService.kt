package kr.flab.f5.f5template.application

import kr.flab.f5.f5template.dto.ProductCreateRequestDto
import kr.flab.f5.f5template.dto.ProductReadResponseDto
import kr.flab.f5.f5template.dto.ProductUpdateRequestDto
import kr.flab.f5.f5template.mysql.jpa.entity.Product

interface ProductService {
    fun decreaseStock(id: Long)
    fun increaseStock(id: Long)
    fun create(createDto: ProductCreateRequestDto)
    fun read(id: Long): Product
    fun update(id: Long, updateDto: ProductUpdateRequestDto): Product
    fun delete(id: Long)
}