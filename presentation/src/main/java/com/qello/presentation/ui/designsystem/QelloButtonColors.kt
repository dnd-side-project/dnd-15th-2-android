package com.qello.presentation.ui.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class QelloButtonColors(
    val lightButtonColors: QelloStateThemeColors,
    val darkButtonColors: QelloStateThemeColors,
)

internal val QelloButtonColorsScheme = QelloButtonColors(
    lightButtonColors = QelloStateThemeColors(
        container = QelloStateColors(
            default = QelloColorPalette.Bule50,
            hover = QelloColorPalette.Bule40,
            press = QelloColorPalette.Bule30
        ),
        label = QelloStateColors(
            default = QelloColorPalette.Neutral100,
            hover = QelloColorPalette.Neutral95,
            press = QelloColorPalette.Neutral90
        )
    ),
    darkButtonColors = QelloStateThemeColors(
        container = QelloStateColors(
            default = QelloColorPalette.Neutral30,
            hover = QelloColorPalette.Neutral25,
            press = QelloColorPalette.Neutral20
        ),
        label = QelloStateColors(
            default = QelloColorPalette.Bule70,
            hover = QelloColorPalette.Bule60,
            press = QelloColorPalette.Bule50
        )
    ),
)

val LocalQelloButtonColorsScheme = staticCompositionLocalOf { QelloButtonColorsScheme }
