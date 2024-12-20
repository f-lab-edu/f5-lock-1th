package kr.flab.f5.f5template.common.exception

class ProductException(
    override val code: ExceptionCode,
    override val message: String?
) : BusinessException(code, message)
