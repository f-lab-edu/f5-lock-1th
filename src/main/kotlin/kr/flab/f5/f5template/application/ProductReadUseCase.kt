package kr.flab.f5.f5template.application

interface ProductReadUseCase {
    fun findProduct(id: Long): ProductVO
}
