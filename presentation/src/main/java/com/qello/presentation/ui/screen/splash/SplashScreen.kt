package com.qello.presentation.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.qello.presentation.R
import com.qello.presentation.ui.designsystem.theme.QelloTheme

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                SplashSideEffect.NavigateToLogin -> onNavigateToLogin()
                SplashSideEffect.NavigateToMain -> onNavigateToMain()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(QelloTheme.colors.background.normalDefault),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(QelloTheme.iconSize.size80),
            painter = painterResource(R.drawable.img_logo),
            contentDescription = null,
        )
    }
}
