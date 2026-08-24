package com.qello.presentation.ui.designsystem

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object QelloGradient {
    val backgroundStrong: Brush = Brush.verticalGradient(
        colors = listOf(QelloColorPalette.Navy10, QelloColorPalette.Bule20),
    )

    val backgroundDefault: Brush = Brush.verticalGradient(
        colors = listOf(QelloColorPalette.Neutral0, QelloColorPalette.Bule10),
    )

    // 좌상단 -> 우하단 대각선. end에 Offset.Infinite를 주면 실제 그려지는 영역 크기에 맞춰
    // 대각선 방향으로 자동으로 늘어남 (컴포넌트 크기가 달라져도 항상 대각선 유지).
    val cardHighlight: Brush = Brush.linearGradient(
        colors = listOf(QelloColorPalette.Bule20, QelloColorPalette.Bule40),
        start = Offset.Zero,
        end = Offset.Infinite,
    )

    // 진행바 채움: 왼쪽은 불투명한 블루, 오른쪽으로 갈수록 투명해져서 끝이 안 보이게 자연스럽게 사라짐
    val progressFill: Brush = Brush.horizontalGradient(
        colors = listOf(QelloColorPalette.Bule30, Color.Transparent),
    )
}
