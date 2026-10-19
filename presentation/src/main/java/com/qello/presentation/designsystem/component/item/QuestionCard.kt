package com.qello.presentation.designsystem.component.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.qello.presentation.R
import com.qello.presentation.designsystem.component.spacer.QelloSpacer
import com.qello.presentation.designsystem.component.text.QelloText
import com.qello.presentation.designsystem.theme.QelloTheme

@Composable
fun QuestionCardContent(
    questionText: String,
    textColor: Color,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isSelected) {
        Row(
            modifier = modifier.padding(QelloTheme.spacing.spacing22),
            horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            QelloText(
                text = questionText,
                style = QelloTheme.typography.body1,
                color = textColor,
                modifier = Modifier.weight(1f),
            )

            Image(
                painter = painterResource(R.drawable.img_heart),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .sizeIn(maxWidth = 140.dp, maxHeight = 80.dp)
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
            )
        }
    } else {
        Column(modifier = modifier.padding(QelloTheme.spacing.spacing22)) {
            QelloText(
                text = questionText,
                style = QelloTheme.typography.body1,
                color = textColor,
            )

            QelloSpacer(QelloTheme.spacing.spacing12)

            // 문구가 길어지거나 화면이 좁으면 남은 공간만큼 이미지를 줄여서 문구와 겹치지 않게 한다
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd,
            ) {
                Image(
                    painter = painterResource(R.drawable.img_heart),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .sizeIn(maxWidth = 140.dp, maxHeight = 80.dp)
                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                )
            }
        }
    }
}
