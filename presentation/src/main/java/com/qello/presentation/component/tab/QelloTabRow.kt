package com.qello.presentation.component.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.theme.QelloTheme

@Composable
fun QelloTabRow(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(QelloTheme.colors.background.normalDefault),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            tabs.forEachIndexed { index, tab ->
                val selected = index == selectedIndex
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                        ) { onTabSelected(index) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    QelloText(
                        text = tab,
                        style = QelloTheme.typography.body1,
                        color = if (selected) QelloTheme.colors.label.normal2 else QelloTheme.colors.label.disabled,
                        modifier = Modifier.padding(vertical = QelloTheme.spacing.spacing12),
                    )

                    Box(modifier = Modifier.fillMaxWidth().height(4.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(QelloTheme.colors.background.normalStrong),
                        )
                        if (selected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth(0.6f)
                                    .fillMaxHeight()
                                    .background(QelloTheme.colors.primary.strong),
                            )
                        }
                    }
                }
            }
        }
    }
}
