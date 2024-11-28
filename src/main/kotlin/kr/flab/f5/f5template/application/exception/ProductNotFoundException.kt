package kr.flab.f5.f5template.application.exception;

class ProductNotFoundException(id: Long) : EntityNotFoundException(id) {
    override val message = "Product with ID : ${id} not found";
}
