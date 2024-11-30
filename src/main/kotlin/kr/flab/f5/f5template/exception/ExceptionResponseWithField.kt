package kr.flab.f5.f5template.exception

import java.time.LocalDateTime

data class ExceptionResponseWithField(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val error: String,
    val message: String,
    val fields: List<ValidationFieldException>
)
