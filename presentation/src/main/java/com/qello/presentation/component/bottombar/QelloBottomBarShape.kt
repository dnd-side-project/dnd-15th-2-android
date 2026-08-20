package com.qello.presentation.component.bottombar

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Density
import com.qello.presentation.R
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.theme.QelloTheme

@Composable
fun QelloBottomBarItem(
    defaultIcon: Painter,
    pressedIcon: Painter,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val isActiveLook = isPressed || selected
    val icon = if (isActiveLook) pressedIcon else defaultIcon
    val labelColor = if (isActiveLook) QelloTheme.colors.label.strong else QelloTheme.colors.label.neutral

    Column(
        modifier = modifier
            .width(QelloTheme.iconSize.size20)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClickLabel = label,
                role = Role.Tab,
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            colorFilter = if (isActiveLook) null else ColorFilter.tint(QelloTheme.colors.label.neutral),
            modifier = Modifier.size(QelloTheme.iconSize.size20),
        )
        QelloText(
            text = label,
            style = QelloTheme.typography.caption3,
            color = labelColor,
            modifier = Modifier.wrapContentWidth(unbounded = true),
        )
    }
}

private const val DesignReferenceWidthDp = 412f

/**
 * 각 기종별 가로 픽셀을 도출하여 알맞은 비율로 보여지게 만듬
 * */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun rememberBottomBarDensity(): Density {
    val configuration = LocalConfiguration.current
    val baseDensity = LocalDensity.current
    val actualWidthDp = configuration.screenWidthDp.toFloat()
    val scale = (actualWidthDp / DesignReferenceWidthDp).coerceAtMost(1f)
    return Density(density = baseDensity.density * scale, fontScale = baseDensity.fontScale)
}

enum class HomeBottomBarTab { HOME, RECEIVED, SENT }

@Composable
fun QelloHomeBottomBar(
    selectedTab: HomeBottomBarTab,
    onTabClick: (HomeBottomBarTab) -> Unit,
    onCenterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val barWidth = dimensionResource(R.dimen.bottom_bar_width)
    val containerHeight = dimensionResource(R.dimen.bottom_bar_container_height)
    val barVisibleHeight = dimensionResource(R.dimen.bottom_bar_visible_height)
    val centerImageSize = dimensionResource(R.dimen.bottom_bar_center_image_size)
    val leftIconStart = dimensionResource(R.dimen.bottom_bar_left_icon_start)
    val leftIconTop = dimensionResource(R.dimen.bottom_bar_left_icon_top)
    val rightIconEnd = dimensionResource(R.dimen.bottom_bar_right_icon_end)
    val rightIconTop = dimensionResource(R.dimen.bottom_bar_right_icon_top)

    CompositionLocalProvider(LocalDensity provides rememberBottomBarDensity()) {
        Box(modifier = modifier.size(barWidth, containerHeight)) {
            Image(
                painter = painterResource(R.drawable.bottom_bar_background),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(barWidth, barVisibleHeight),
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(barWidth, barVisibleHeight),
            ) {
                QelloBottomBarItem(
                    defaultIcon = painterResource(R.drawable.icon_myfeed_default),
                    pressedIcon = painterResource(R.drawable.icon_myfeed_hover),
                    label = stringResource(R.string.bottom_bar_received_label),
                    selected = selectedTab == HomeBottomBarTab.RECEIVED,
                    onClick = { onTabClick(HomeBottomBarTab.RECEIVED) },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = leftIconStart, y = leftIconTop),
                )

                QelloBottomBarItem(
                    defaultIcon = painterResource(R.drawable.icon_sendfeed_default),
                    pressedIcon = painterResource(R.drawable.icon_sendfeed_hover),
                    label = stringResource(R.string.bottom_bar_sent_label),
                    selected = selectedTab == HomeBottomBarTab.SENT,
                    onClick = { onTabClick(HomeBottomBarTab.SENT) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = -rightIconEnd, y = rightIconTop),
                )
            }

            val centerInteractionSource = remember { MutableInteractionSource() }
            val isCenterPressed by centerInteractionSource.collectIsPressedAsState()
            val centerImageRes = when (selectedTab) {
                HomeBottomBarTab.HOME ->
                    if (isCenterPressed) R.drawable.bottom_bar_center_send_pressed else R.drawable.bottom_bar_center_send_default

                HomeBottomBarTab.RECEIVED, HomeBottomBarTab.SENT ->
                    if (isCenterPressed) R.drawable.bottom_bar_center_globe_pressed else R.drawable.bottom_bar_center_globe_default
            }

            Image(
                painter = painterResource(centerImageRes),
                contentDescription = if (selectedTab == HomeBottomBarTab.HOME) "질문 보내기" else "홈으로 이동",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = dimensionResource(R.dimen.bottom_bar_center_image_offset_x), y = dimensionResource(R.dimen.bottom_bar_center_image_offset_y))
                    .size(centerImageSize)
                    .clickable(
                        interactionSource = centerInteractionSource,
                        indication = null,
                        role = Role.Button,
                        onClick = onCenterClick,
                    ),
            )
        }
    }
}
