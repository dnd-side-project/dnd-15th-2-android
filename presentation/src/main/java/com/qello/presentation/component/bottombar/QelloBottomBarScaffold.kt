package com.qello.presentation.component.bottombar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.qello.presentation.ui.theme.QelloTheme

/**
 * 하단바가 있는 화면에서 사용할 Scaffold
 * */
@Composable
fun QelloBottomBarScaffold(
    selectedTab: HomeBottomBarTab,
    onTabClick: (HomeBottomBarTab) -> Unit,
    onCenterClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()
        QelloHomeBottomBar(
            selectedTab = selectedTab,
            onTabClick = onTabClick,
            onCenterClick = onCenterClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = QelloTheme.spacing.spacing17, end = QelloTheme.spacing.spacing17, bottom = QelloTheme.spacing.spacing36),
        )
    }
}
