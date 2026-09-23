package com.qello.presentation.common

import androidx.annotation.StringRes
import com.qello.domain.result.AppError
import com.qello.presentation.R

@StringRes
fun AppError.toMessageRes(): Int = when (this) {
    is AppError.Network -> R.string.error_network
    is AppError.Server -> when (status) {
        401 -> R.string.error_unauthorized
        403 -> R.string.error_forbidden
        404 -> R.string.error_not_found
        429 -> R.string.error_too_many_requests
        503 -> R.string.error_service_unavailable
        in 500..599 -> R.string.error_server
        else -> R.string.error_request_failed
    }
}
