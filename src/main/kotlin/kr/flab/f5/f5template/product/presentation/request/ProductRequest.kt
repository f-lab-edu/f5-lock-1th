package kr.flab.f5.f5template.product.presentation.request

import kr.flab.f5.f5template.product.common.util.RegexpConstants
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class ProductRequest(
    @field:Size(max = 20, message = "이름은 20글자 이하이어야 합니다.")
    @field:Pattern(regexp = RegexpConstants.ENGLISH_KOREAN_REGEXP, message = "이름은 한국어 또는 영어 이어야 합니다.")
    val name: String,

    @field:Min(value = 1, message = "가격은 1 이상이어야 합니다.")
    val price: Long,

    @field:Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    val stock: Long
)