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
import com.qello.domain.model.ReportReason
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.item.QelloActionSheetItem
import com.qello.presentation.ui.designsystem.theme.QelloTheme

private val reportReasons = listOf(
    "성적 또는 노골적인 콘텐츠" to ReportReason.SEXUAL_CONTENT,
    "폭력 및 위험" to ReportReason.VIOLENCE_OR_THREAT,
    "혐오 및 괴롭힘" to ReportReason.HATE_OR_HARASSMENT,
    "개인정보 노출" to ReportReason.PRIVACY_VIOLATION,
    "스팸 및 광고" to ReportReason.SPAM_OR_ADVERTISING,
    "사칭" to ReportReason.IMPERSONATION,
    "불법 거래 및 위험 행동" to ReportReason.ILLEGAL_OR_DANGEROUS,
    "기타" to ReportReason.OTHER,
)

/** 질문글/답변 카드의 "..."를 눌렀을 때 뜨는 신고 사유 선택 시트. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QelloReportBottomSheet(
    onDismissRequest: () -> Unit,
    onReasonSelected: (reasonCode: ReportReason) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.Transparent,
        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    QelloTheme.gradient.backgroundStrong,
                    RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                )
                .padding(top = 40.dp, bottom = QelloTheme.spacing.spacing20),
        ) {
            reportReasons.forEachIndexed { index, (text, reasonCode) ->
                if (index != 0) {
                    Spacer(Modifier.height(QelloTheme.spacing.spacing12))
                }

                QelloActionSheetItem(
                    text = text,
                    onClick = { onReasonSelected(reasonCode) },
                    modifier = Modifier.padding(horizontal = 30.dp),
                    showLeadingDot = false,
                )
            }

            Spacer(Modifier.height(50.dp))

            QelloLargeButton(
                text = "닫기",
                colors = QelloTheme.buttonColors.darkButtonColors,
                onClick = onDismissRequest,
                modifier = Modifier.padding(horizontal = 18.dp),
            )
        }
    }
}
