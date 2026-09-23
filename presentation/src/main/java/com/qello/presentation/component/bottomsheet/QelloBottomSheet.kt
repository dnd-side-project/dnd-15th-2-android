package com.qello.presentation.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.qello.presentation.ui.designsystem.theme.QelloTheme

/**
 * Qello 기본 모달 바텀시트. 배경 그라디언트와 상단 라운딩을 포함한다.
 *
 * [onDismissRequest]에 빈 람다를 넘기면 안된다.
 * 유령시트로 남아있을수도 있기 때문이다.
 * @param content 상하/좌우 패딩이 적용된 [Column] 스코프. content는 수직으로 쌓인다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QelloBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = QelloTheme.spacing.spacing20,
    verticalPadding: Dp = QelloTheme.spacing.spacing38,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.Transparent,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    QelloTheme.gradient.backgroundStrong,
                    RoundedCornerShape(
                        topStart = QelloTheme.radius.radius24,
                        topEnd = QelloTheme.radius.radius24
                    ),
                )
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            content = content,
        )
    }
}
