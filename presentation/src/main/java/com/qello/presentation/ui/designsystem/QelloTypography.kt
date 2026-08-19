package com.qello.presentation.ui.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.qello.presentation.R

private val suitStyle = FontFamily(
    Font(resId = R.font.suit_bold, weight = FontWeight.Bold),
    Font(resId = R.font.suit_semibold, weight = FontWeight.SemiBold),
    Font(resId = R.font.suit_medium, weight = FontWeight.Medium)
)

private val baseTextStyle = TextStyle(
    fontFamily = suitStyle,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)

@Immutable
data class QelloTypography(
    val heading1: TextStyle,
    val heading2: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val caption1: TextStyle,
    val caption2: TextStyle,
    val caption3: TextStyle,
    val button: TextStyle,
    val category: TextStyle
)

val Typography = QelloTypography(
    heading1 = baseTextStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 36.sp,
        letterSpacing = (-1.6).sp
    ),
    heading2 = baseTextStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 33.sp,
        letterSpacing = (-1.6).sp
    ),
    body1 = baseTextStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.8).sp
    ),
    body2 = baseTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = (-0.8).sp
    ),
    caption1 = baseTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.8).sp
    ),
    caption2 = baseTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 19.6.sp,
        letterSpacing = (-0.8).sp
    ),
    caption3 = baseTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 17.sp,
        letterSpacing = (-0.8).sp
    ),
    button = baseTextStyle.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 27.sp,
        letterSpacing = (-0.8).sp
    ),
    category = baseTextStyle.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.5.sp,
        letterSpacing = (-0.8).sp
    )
)

internal val LocalQelloTypography = staticCompositionLocalOf { Typography }
