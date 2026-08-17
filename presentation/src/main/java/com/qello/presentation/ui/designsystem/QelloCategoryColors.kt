package com.qello.presentation.ui.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class QelloCategoryColors(
    val lightButtonColors: QelloStateThemeColors,
    val darkButtonColors: QelloStateThemeColors,
)

internal val QelloCategoryColorsScheme = QelloCategoryColors(
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
            default = QelloColorPalette.Navy30,
            hover = QelloColorPalette.Navy20,
            press = QelloColorPalette.Navy10
        ),
        label = QelloStateColors(
            default = QelloColorPalette.Navy70,
            hover = QelloColorPalette.Navy60,
            press = QelloColorPalette.Navy50
        )
    ),
)

val LocalQelloCategoryColorsScheme = staticCompositionLocalOf { QelloCategoryColorsScheme }
