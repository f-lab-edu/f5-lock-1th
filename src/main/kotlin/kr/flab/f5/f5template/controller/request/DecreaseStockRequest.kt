package kr.flab.f5.f5template.controller.request

import java.math.BigInteger

data class DecreaseStockRequest(
    val orderNo: BigInteger,
    val quantity: Int
)
