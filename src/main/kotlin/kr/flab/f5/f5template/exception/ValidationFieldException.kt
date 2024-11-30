package kr.flab.f5.f5template.exception

data class ValidationFieldException(
    val field: String,
    val value: String?,
    val message: String
)
