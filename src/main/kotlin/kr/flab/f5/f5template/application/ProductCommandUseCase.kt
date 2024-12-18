package kr.flab.f5.f5template.application

interface ProductCommandUseCase {
    fun decreaseStock(id: Long)
    fun createProduct(product: ProductVO)
    fun updateProduct(id: Long, product: ProductVO)
    fun deleteProduct(id: Long)
}
