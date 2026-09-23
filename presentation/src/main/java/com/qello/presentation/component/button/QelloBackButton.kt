package com.qello.presentation.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.qello.presentation.R
import com.qello.presentation.ui.designsystem.QelloColorPalette
import com.qello.presentation.ui.designsystem.theme.QelloTheme

@Composable
fun QelloBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    QelloSquareIconButton(
        painter = painterResource(R.drawable.ic_back),
        onClick = onClick,
        modifier = modifier,
    )
}
