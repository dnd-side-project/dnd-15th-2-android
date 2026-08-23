package com.qello.presentation.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.qello.presentation.R

@Composable
fun QelloMoreButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    QelloSquareIconButton(
        painter = painterResource(R.drawable.ic_more),
        onClick = onClick,
        modifier = modifier,
        iconRotation = 90f,
    )
}
