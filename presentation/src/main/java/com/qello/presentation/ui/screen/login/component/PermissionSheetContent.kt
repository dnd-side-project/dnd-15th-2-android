package com.qello.presentation.ui.screen.login.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.spacer.QelloSpacer
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.screen.login.permission.LoginPermission
import com.qello.presentation.ui.designsystem.theme.QelloTheme

@Composable
fun PermissionSheetContent(
    isLocationGranted: Boolean,
    isNotificationGranted: Boolean,
    onPermissionItemClick: (LoginPermission) -> Unit,
    onAllowClick: () -> Unit,
) {
    QelloText(
        text = stringResource(R.string.login_permission_title),
        style = QelloTheme.typography.heading1,
        color = QelloTheme.colors.label.strong,
    )

    QelloSpacer(QelloTheme.spacing.spacing8)

    QelloText(
        text = stringResource(R.string.login_permission_description),
        style = QelloTheme.typography.caption1,
        color = QelloTheme.colors.label.normal2,
    )

    QelloSpacer(QelloTheme.spacing.spacing24)

    PermissionInfoItem(
        title = stringResource(R.string.login_permission_location_title),
        description = stringResource(R.string.login_permission_location_description),
        iconPainter = painterResource(R.drawable.icon_my_location),
        isGranted = isLocationGranted,
        onClick = { onPermissionItemClick(LoginPermission.LOCATION) },
    )

    QelloSpacer(QelloTheme.spacing.spacing12)

    PermissionInfoItem(
        title = stringResource(R.string.login_permission_notification_title),
        description = stringResource(R.string.login_permission_notification_description),
        iconPainter = painterResource(R.drawable.icon_bell),
        isGranted = isNotificationGranted,
        onClick = { onPermissionItemClick(LoginPermission.NOTIFICATION) },
    )

    QelloSpacer(QelloTheme.spacing.spacing36)

    QelloLargeButton(text = stringResource(R.string.login_permission_allow), onClick = onAllowClick)
}

@Composable
private fun PermissionInfoItem(
    title: String,
    description: String,
    iconPainter: Painter,
    isGranted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val badgeColor = if (isGranted) {
        QelloTheme.colors.primary.normal
    } else {
        QelloTheme.colors.status.destructive
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(QelloTheme.radius.radius16))
            .background(QelloTheme.colors.background.elevatedStrong)
            .clickable(enabled = !isGranted, onClick = onClick)
            .padding(vertical = QelloTheme.spacing.spacing14, horizontal = QelloTheme.spacing.spacing16),
        horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(QelloTheme.iconSize.size40)
                .clip(CircleShape)
                .background(badgeColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = iconPainter,
                contentDescription = null,
                tint = QelloTheme.colors.label.strong,
                modifier = Modifier.size(QelloTheme.iconSize.size24),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            QelloText(
                text = title,
                style = QelloTheme.typography.body1,
                color = QelloTheme.colors.label.strong,
            )

            QelloSpacer(QelloTheme.spacing.spacing4)

            QelloText(
                text = description,
                style = QelloTheme.typography.caption2,
                color = QelloTheme.colors.label.neutral,
            )
        }
    }
}
