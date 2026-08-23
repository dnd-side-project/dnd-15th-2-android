package com.qello.presentation.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.qello.presentation.R

@Composable
fun QelloShareButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    QelloSquareIconButton(
        painter = painterResource(R.drawable.icon_upload),
        onClick = onClick,
        modifier = modifier,
    )
}
