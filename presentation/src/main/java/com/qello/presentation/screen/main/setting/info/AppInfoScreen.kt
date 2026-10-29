package com.qello.presentation.screen.main.setting.info

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import com.qello.presentation.R
import com.qello.presentation.designsystem.component.button.QelloBackButton
import com.qello.presentation.designsystem.component.spacer.QelloSpacer
import com.qello.presentation.designsystem.component.text.QelloText
import com.qello.presentation.designsystem.theme.QelloColorPalette
import com.qello.presentation.designsystem.theme.QelloTheme
import com.qello.presentation.screen.main.setting.component.SettingCard
import com.qello.presentation.screen.main.setting.component.SettingDivider
import com.qello.presentation.screen.main.setting.component.SettingMenuRow
import com.qello.presentation.screen.main.setting.component.SettingSectionTitle

private const val MAPBOX_URL = "https://www.mapbox.com/about/maps/"
private const val OPENSTREETMAP_URL = "https://www.openstreetmap.org/copyright"
private const val MAPBOX_PRIVACY_POLICY_URL = "https://www.mapbox.com/legal/privacy#product-privacy-policy"

private const val CHEVRON_COLLAPSED_DEGREES = 0f
private const val CHEVRON_EXPANDED_DEGREES = 90f

private enum class AppInfoTerms(
    @StringRes val title: Int,
    @StringRes val content: Int,
) {
    SERVICE(R.string.app_info_terms_service, R.string.app_info_terms_placeholder),
    PRIVACY(R.string.app_info_terms_privacy, R.string.app_info_terms_placeholder),
    LOCATION(R.string.app_info_terms_location, R.string.app_info_terms_placeholder),
}

@Composable
fun AppInfoScreen(
    onBack: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val versionName = rememberAppVersionName()

    var expandedTerms by rememberSaveable { mutableStateOf<AppInfoTerms?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(QelloTheme.colors.background.normalStrong),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = QelloTheme.spacing.spacing20,
                    end = QelloTheme.spacing.spacing20,
                    top = QelloTheme.spacing.spacing36,
                    bottom = QelloTheme.spacing.spacing24,
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(QelloTheme.iconSize.size48),
                contentAlignment = Alignment.Center,
            ) {
                QelloText(
                    text = stringResource(R.string.app_info_title),
                    style = QelloTheme.typography.heading2,
                    color = QelloTheme.colors.label.strong,
                )
            }

            QelloSpacer(QelloTheme.spacing.spacing36)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.img_logo),
                    contentDescription = null,
                    modifier = Modifier.size(QelloTheme.iconSize.size80),
                )

                QelloSpacer(QelloTheme.spacing.spacing16)

                QelloText(
                    text = stringResource(R.string.app_info_version, versionName),
                    style = QelloTheme.typography.caption3,
                    color = QelloTheme.colors.label.neutral,
                    modifier = Modifier
                        .background(QelloColorPalette.Navy20, RoundedCornerShape(QelloTheme.radius.radiusFull))
                        .border(
                            QelloTheme.borderWidth.border1,
                            QelloTheme.colors.line.normal,
                            RoundedCornerShape(QelloTheme.radius.radiusFull),
                        )
                        .padding(
                            horizontal = QelloTheme.spacing.spacing12,
                            vertical = QelloTheme.spacing.spacing4,
                        ),
                )
            }

            QelloSpacer(QelloTheme.spacing.spacing40)

            SettingSectionTitle(text = stringResource(R.string.app_info_section_terms))

            SettingCard {
                AppInfoTerms.entries.forEachIndexed { index, terms ->
                    if (index > 0) SettingDivider()

                    TermsAccordionItem(
                        title = stringResource(terms.title),
                        content = stringResource(terms.content),
                        isExpanded = expandedTerms == terms,
                        onToggle = { expandedTerms = if (expandedTerms == terms) null else terms },
                    )
                }
            }

            QelloSpacer(QelloTheme.spacing.spacing24)

            SettingSectionTitle(text = stringResource(R.string.app_info_section_map))

            SettingCard {
                SettingMenuRow(
                    label = stringResource(R.string.app_info_map_mapbox),
                    onClick = { uriHandler.openUri(MAPBOX_URL) },
                )

                SettingDivider()

                SettingMenuRow(
                    label = stringResource(R.string.app_info_map_openstreetmap),
                    onClick = { uriHandler.openUri(OPENSTREETMAP_URL) },
                )

                SettingDivider()

                SettingMenuRow(
                    label = stringResource(R.string.app_info_map_privacy_policy),
                    onClick = { uriHandler.openUri(MAPBOX_PRIVACY_POLICY_URL) },
                )
            }

            QelloSpacer(QelloTheme.spacing.spacing40)

            QelloText(
                text = stringResource(
                    R.string.app_info_copyright,
                    stringResource(R.string.app_info_company_name),
                ),
                style = QelloTheme.typography.caption3.copy(textAlign = TextAlign.Center),
                color = QelloTheme.colors.label.assistive,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        QelloBackButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = QelloTheme.spacing.spacing18, top = QelloTheme.spacing.spacing36),
        )
    }
}

@Composable
private fun TermsAccordionItem(
    title: String,
    content: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) CHEVRON_EXPANDED_DEGREES else CHEVRON_COLLAPSED_DEGREES,
        label = "termsChevronRotation",
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isPressed) QelloColorPalette.Navy30 else Color.Transparent)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClick = onToggle,
                )
                .padding(
                    horizontal = QelloTheme.spacing.spacing16,
                    vertical = QelloTheme.spacing.spacing16,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            QelloText(
                text = title,
                style = QelloTheme.typography.body2,
                color = QelloTheme.colors.label.normal1,
                modifier = Modifier.weight(1f),
            )

            Icon(
                painter = painterResource(R.drawable.ic_right),
                contentDescription = null,
                tint = QelloTheme.colors.label.assistive,
                modifier = Modifier.rotate(chevronRotation),
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            QelloText(
                text = content,
                style = QelloTheme.typography.caption2,
                color = QelloTheme.colors.label.alternative,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = QelloTheme.spacing.spacing16,
                        end = QelloTheme.spacing.spacing16,
                        bottom = QelloTheme.spacing.spacing16,
                    ),
            )
        }
    }
}

/** 설치된 앱 정보에서 버전을 읽는다. */
@Composable
private fun rememberAppVersionName(): String {
    val context = LocalContext.current
    return remember(context) {
        val packageManager = context.packageManager
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(context.packageName, 0)
        }
        packageInfo.versionName.orEmpty()
    }
}
