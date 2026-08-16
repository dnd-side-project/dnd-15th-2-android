package com.qello.presentation.ui.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class QelloPrimaryColors(
    val normal: Color,
    val strong: Color,
    val heavy: Color
)

@Immutable
data class QelloLabelColors(
    val normal1: Color,
    val strong: Color,
    val neutral: Color,
    val alternative: Color,
    val assistive: Color,
    val disabled: Color,
    val normal2: Color,
)

@Immutable
data class QelloBackgroundColors(
    val normalDefault: Color,
    val normalStrong: Color,
    val elevatedNormal: Color,
    val elevatedStrong: Color,
)

@Immutable
data class QelloLineColors(
    val normal: Color,
    val strong: Color
)

@Immutable
data class QelloStatusColors(
    val positive: Color,
    val cautionary: Color,
    val destructive: Color
)

@Immutable
data class QelloShadowColors(
    val cautionary: Color,
    val destructive: Color
)

@Immutable
data class QelloImagefieldColors(
    val default: Color,
    val hover: Color
)

@Immutable
data class QelloColors(
    val primary: QelloPrimaryColors,
    val label: QelloLabelColors,
    val background: QelloBackgroundColors,
    val line: QelloLineColors,
    val status: QelloStatusColors,
    val shadow: QelloShadowColors,
    val imagefield: QelloImagefieldColors
)

internal val QelloColorScheme = QelloColors(
    primary = QelloPrimaryColors(
        normal = QelloColorPalette.Bule50,
        strong = QelloColorPalette.Bule40,
        heavy = QelloColorPalette.Bule30
    ),
    label = QelloLabelColors(
        normal1 = QelloColorPalette.Neutral95,
        strong = QelloColorPalette.Neutral100,
        neutral = QelloColorPalette.Neutral80,
        alternative = QelloColorPalette.Neutral75,
        assistive = QelloColorPalette.Neutral70,
        disabled = QelloColorPalette.Neutral40,
        normal2 = QelloColorPalette.Bule40
    ),
    background = QelloBackgroundColors(
        normalDefault = QelloColorPalette.Neutral0,
        normalStrong = QelloColorPalette.Navy10,
        elevatedNormal = QelloColorPalette.Neutral0,
        elevatedStrong = QelloColorPalette.Navy10
    ),
    line = QelloLineColors(
        normal = QelloColorPalette.Neutral25,
        strong = QelloColorPalette.Neutral40
    ),
    status = QelloStatusColors(
        positive = QelloColorPalette.Green50,
        cautionary = QelloColorPalette.Orange50,
        destructive = QelloColorPalette.Red50
    ),
    shadow = QelloShadowColors(
        cautionary = QelloColorPalette.Orange50,
        destructive = QelloColorPalette.Red50
    ),
    imagefield = QelloImagefieldColors(
        default = QelloColorPalette.Navy20,
        hover = QelloColorPalette.Navy10
    )
)

val LocalQelloColorScheme = staticCompositionLocalOf { QelloColorScheme }
