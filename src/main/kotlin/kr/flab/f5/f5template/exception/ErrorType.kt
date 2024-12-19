package kr.flab.f5.f5template.exception

enum class ErrorType(description: String) {
    NO_RESOURCE("존재하지 않는 리소스입니다."),
    UNKNOWN("알 수 없는 에러입니다."),
    INVALID_PARAMETER("잘못된 요청값입니다."), ;
}
