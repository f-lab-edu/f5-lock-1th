package kr.flab.f5.f5template.application.exception

open class EntityNotFoundException(
    id: Long
) : RuntimeException() {
    override val message = "Entity with ID : $id not found"
}