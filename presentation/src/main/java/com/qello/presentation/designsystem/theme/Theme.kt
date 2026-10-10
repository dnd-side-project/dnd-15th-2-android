package com.qello.presentation.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun QelloTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalQelloColorScheme provides QelloColorScheme,
        LocalQelloButtonColorsScheme provides QelloButtonColorsScheme,
        LocalQelloCategoryColorsScheme provides QelloCategoryColorsScheme,
        LocalQelloTextFieldColorsScheme provides QelloTextFieldColorsScheme,
        LocalQelloTypography provides Typography,
    ) {
        content()
    }
}

object QelloTheme {
    val colors: QelloColors
        @Composable
        @ReadOnlyComposable
        get() = LocalQelloColorScheme.current

    val buttonColors: QelloButtonColors
        @Composable
        @ReadOnlyComposable
        get() = LocalQelloButtonColorsScheme.current

    val categoryColors: QelloCategoryColors
        @Composable
        @ReadOnlyComposable
        get() = LocalQelloCategoryColorsScheme.current

    val textFieldColors: QelloTextFieldColors
        @Composable
        @ReadOnlyComposable
        get() = LocalQelloTextFieldColorsScheme.current

    val typography: QelloTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalQelloTypography.current

    val spacing: QelloSpacing
        get() = QelloSpacing

    val radius: QelloRadius
        get() = QelloRadius

    val iconSize: QelloIconSize
        get() = QelloIconSize

    val borderWidth: QelloBorderWidth
        get() = QelloBorderWidth

    val gradient: QelloGradient
        get() = QelloGradient
}
