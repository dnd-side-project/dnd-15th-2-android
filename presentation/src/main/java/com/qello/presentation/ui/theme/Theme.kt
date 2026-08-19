package com.qello.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.qello.presentation.ui.designsystem.LocalQelloButtonColorsScheme
import com.qello.presentation.ui.designsystem.LocalQelloCategoryColorsScheme
import com.qello.presentation.ui.designsystem.LocalQelloColorScheme
import com.qello.presentation.ui.designsystem.LocalQelloTextFieldColorsScheme
import com.qello.presentation.ui.designsystem.LocalQelloTypography
import com.qello.presentation.ui.designsystem.QelloBorderWidth
import com.qello.presentation.ui.designsystem.QelloButtonColors
import com.qello.presentation.ui.designsystem.QelloButtonColorsScheme
import com.qello.presentation.ui.designsystem.QelloCategoryColors
import com.qello.presentation.ui.designsystem.QelloCategoryColorsScheme
import com.qello.presentation.ui.designsystem.QelloColorScheme
import com.qello.presentation.ui.designsystem.QelloColors
import com.qello.presentation.ui.designsystem.QelloGradient
import com.qello.presentation.ui.designsystem.QelloIconSize
import com.qello.presentation.ui.designsystem.QelloRadius
import com.qello.presentation.ui.designsystem.QelloSpacing
import com.qello.presentation.ui.designsystem.QelloTextFieldColors
import com.qello.presentation.ui.designsystem.QelloTextFieldColorsScheme
import com.qello.presentation.ui.designsystem.QelloTypography
import com.qello.presentation.ui.designsystem.Typography

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
