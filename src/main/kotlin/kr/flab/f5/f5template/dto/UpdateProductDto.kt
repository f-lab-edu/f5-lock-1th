package kr.flab.f5.f5template.dto

import com.fasterxml.jackson.annotation.JsonProperty


data class UpdateProductDto(
    @JsonProperty("name") val name: String,
    @JsonProperty("price") val price: Long,
    @JsonProperty("quantity") val quantity: Long
)
