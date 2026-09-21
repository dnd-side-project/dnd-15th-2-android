package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val data: T,
)

@Serializable
data class ErrorResponse(
    val errorDetail: ErrorDetail? = null,
    val message: String? = null,
)

@Serializable
data class ErrorDetail(
    val code: String? = null,
    val field: String? = null,
    val reason: String? = null,
)
