package com.qello.presentation.ui.screen.main.question

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.qello.presentation.R
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.theme.QelloTheme

/** 질문을 보내는 중/보냈어요 화면 가운데에 들어가는 이미지. 두 화면이 같은 크기와 위치를 쓴다. */
@Composable
fun QuestionStatusImage(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int = R.drawable.img_flight,
) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier.size(300.dp),
    )
}

/** 질문을 보내는 중/보냈어요 화면이 같은 위치에 같은 글씨를 보여주도록 공유하는 상단 문구 영역. */
@Composable
fun QuestionStatusHeader(
    titleLine1: String,
    titleLine2: String,
    caption: String,
) {
    // 뒤로가기 버튼이 있는 화면(패딩 24dp + 버튼 48dp)과 버튼-텍스트 간격(57dp)을 더해 시작 위치를 맞춤
    Spacer(Modifier.height(24.dp + QelloTheme.iconSize.size48 + 57.dp))

    Column(modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing4)) {
        QelloText(
            text = titleLine1,
            style = QelloTheme.typography.heading1,
            color = QelloTheme.colors.label.strong,
        )

        QelloText(
            text = titleLine2,
            style = QelloTheme.typography.heading1,
            color = QelloTheme.colors.label.strong,
        )

        Spacer(Modifier.height(QelloTheme.spacing.spacing8))

        QelloText(
            text = caption,
            style = QelloTheme.typography.caption1,
            color = QelloTheme.colors.primary.normal,
        )
    }
}
