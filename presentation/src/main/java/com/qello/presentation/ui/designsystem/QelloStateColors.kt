package com.qello.presentation.ui.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class QelloStateColors(
    val default: Color,
    val hover: Color,
    val press: Color,
)

@Immutable
data class QelloStateThemeColors(
    val container: QelloStateColors,
    val label: QelloStateColors,
)
