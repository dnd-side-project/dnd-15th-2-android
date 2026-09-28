package com.qello.presentation.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.theme.QelloTheme

/**
 * 신고 사유를 고른 뒤 뜨는 완료 안내.
 * 바텀바가 있는 화면에서도 항상 맨 위에 뜨도록 일반 오버레이가 아니라 바텀시트로 띄운다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QelloReportCompleteOverlay(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onCloseClick,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.Transparent,
        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    QelloTheme.gradient.completeCardHighlight,
                    RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                )
                .padding(
                    top = QelloTheme.spacing.spacing38,
                    start = QelloTheme.spacing.spacing20,
                    end = QelloTheme.spacing.spacing20,
                    bottom = QelloTheme.spacing.spacing20,
                ),
        ) {
            QelloText(
                text = "신고가 완료됐어요!",
                style = QelloTheme.typography.heading1,
                color = QelloTheme.colors.label.strong,
            )

            Spacer(Modifier.height(QelloTheme.spacing.spacing8))

            QelloText(
                text = "궁금한 것을 질문하고, 다양한 사람들의 답변을 받아보세요.",
                style = QelloTheme.typography.caption1,
                color = QelloTheme.colors.label.normal2,
            )

            Spacer(Modifier.height(QelloTheme.spacing.spacing62))

            QelloLargeButton(
                text = "닫기",
                onClick = onCloseClick,
            )
        }
    }
}
