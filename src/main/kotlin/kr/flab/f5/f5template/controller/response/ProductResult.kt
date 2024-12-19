package kr.flab.f5.f5template.controller.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProductResult(
    val id: Long,
    val name: String,
    val stock: Long? = null,
    val price: Long? = null
)
