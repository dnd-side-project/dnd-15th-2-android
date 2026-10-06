package com.qello.presentation.ui.designsystem

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush

private const val CAROUSEL_DARK_STOP = 0.11f
private const val CAROUSEL_START_OVERSHOOT = 0.12f
private const val CAROUSEL_END_OVERSHOOT_X = 0.12f
private const val CAROUSEL_END_OVERSHOOT_Y = 0.09f

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

    // 질문 선택 캐러셀 카드. 피그마 그라데이션 핸들을 카드 크기에 맞춰 옮긴 것이다.
    // 선은 카드 좌상단 위쪽 바깥에서 시작해 우하단 바깥에서 끝나고, 어두운 색은 선의 11% 지점까지 유지된다.
    val carouselCardHighlight: Brush = object : ShaderBrush() {
        override fun createShader(size: Size): Shader = LinearGradientShader(
            from = Offset(0f, -size.height * CAROUSEL_START_OVERSHOOT),
            to = Offset(size.width * (1f + CAROUSEL_END_OVERSHOOT_X), size.height * (1f + CAROUSEL_END_OVERSHOOT_Y)),
            colors = listOf(QelloColorPalette.Bule10, QelloColorPalette.Bule40),
            colorStops = listOf(CAROUSEL_DARK_STOP, 1f),
        )
    }

    // 진행바 채움: 왼쪽은 불투명한 블루, 오른쪽으로 갈수록 투명해져서 끝이 안 보이게 자연스럽게 사라짐
    val progressFill: Brush = Brush.horizontalGradient(
        colors = listOf(QelloColorPalette.Bule30, Color.Transparent),
    )

    val completeCardHighlight: Brush = Brush.verticalGradient(
        colorStops = arrayOf(0.06f to QelloColorPalette.Neutral0, 0.97f to QelloColorPalette.Navy20),
    )
}
