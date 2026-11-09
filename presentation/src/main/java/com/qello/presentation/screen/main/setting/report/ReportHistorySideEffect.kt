package com.qello.presentation.screen.main.setting.report

import androidx.annotation.StringRes

sealed interface ReportHistorySideEffect {
    data class ShowSnackbar(@StringRes val message: Int) : ReportHistorySideEffect
}
