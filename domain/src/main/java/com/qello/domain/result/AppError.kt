package com.qello.domain.result

sealed class AppError(
    message: String?,
    cause: Throwable? = null,
) : Exception(message, cause) {

    /**
     * 서버가 응답한 실패. 공통 에러 JSON을 그대로 담는다.
     */
    class Server(
        val status: Int,
        val code: String?,
        val field: String?,
        val reason: String?,
        message: String?,
    ) : AppError(message)

    /** 서버 응답을 받지 못함 (연결 실패, 타임아웃) */
    class Network(cause: Throwable) : AppError(cause.message, cause)
}
