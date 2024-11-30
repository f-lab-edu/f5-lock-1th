package kr.flab.f5.f5template.exception

import java.time.LocalDateTime

data class ExceptionResponse (
    val timeStamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String?
)