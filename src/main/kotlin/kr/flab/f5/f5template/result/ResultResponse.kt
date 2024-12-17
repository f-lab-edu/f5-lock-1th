package kr.flab.f5.f5template.result

import org.springframework.http.HttpStatus

class ResultResponse (
    val status: HttpStatus,
    val message: String,
    val data: Any? = null
){
    companion object {
        fun of(resultCode: ResultCode): ResultResponse {
            return ResultResponse(resultCode, "")
        }
        fun of(resultCode: ResultCode, data: Any?): ResultResponse {
            return ResultResponse(resultCode, data)
        }
    }

    private constructor (resultCode: ResultCode, data: Any?) : this(resultCode.status, resultCode.message, data)
}