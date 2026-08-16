package com.qello.presentation.ui.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class QelloTextFieldLabelColors(val default: Color, val destructive: Color, val active: Color)

@Immutable
data class QelloTextFieldLineColors(val default: Color, val focus: Color, val destructive: Color)

@Immutable
data class QelloTextFieldColors(
    val container: Color,
    val label: QelloTextFieldLabelColors,
    val line: QelloTextFieldLineColors,
)

internal val QelloTextFieldColorsScheme = QelloTextFieldColors(
    container = QelloColorPalette.Navy10,
    label = QelloTextFieldLabelColors(
        default = QelloColorPalette.Navy40,
        destructive = QelloColorPalette.Red40,
        active = QelloColorPalette.Navy95
    ),
    line = QelloTextFieldLineColors(
        default = QelloColorPalette.Navy30,
        focus = QelloColorPalette.Navy40,
        destructive = QelloColorPalette.Red40
    )
)

val LocalQelloTextFieldColorsScheme = staticCompositionLocalOf { QelloTextFieldColorsScheme }
