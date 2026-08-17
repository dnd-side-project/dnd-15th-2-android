package com.qello.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.qello.presentation.ui.designsystem.QelloTextFieldColors
import com.qello.presentation.ui.theme.QelloTheme

@Composable
fun QelloTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    isError: Boolean = false,
    supportingText: String? = null,
) {
    val colors = QelloTheme.textFieldColors

    Column(modifier = modifier) {
        QelloText(
            text = label,
            modifier = Modifier.padding(start = QelloTheme.spacing.spacing4),
            style = QelloTheme.typography.body1,
            color = QelloTheme.colors.label.normal1,
        )

        Spacer(Modifier.height(QelloTheme.spacing.spacing12))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = {
                QelloText(text = "텍스트를 입력해주세요.", style = QelloTheme.typography.body2, color = colors.label.default)
            },
            singleLine = true,
            isError = isError,
            trailingIcon = if (isError) {
                {
                    // QelloIcon 생기면 실제 경고 아이콘으로 교체
                    Box(
                        modifier = Modifier
                            .size(QelloTheme.iconSize.size24)
                            .clip(CircleShape)
                            .background(QelloTheme.colors.status.destructive),
                    )
                }
            } else null,
            shape = RoundedCornerShape(QelloTheme.radius.radius20),
            colors = qelloOutlinedTextFieldColors(colors),
        )

        if (isError && supportingText != null) {
            Spacer(Modifier.height(QelloTheme.spacing.spacing8))

            QelloText(
                text = supportingText,
                modifier = Modifier.padding(start = QelloTheme.spacing.spacing16),
                style = QelloTheme.typography.caption2,
                color = colors.label.destructive
            )
        }
    }
}

@Composable
fun QelloTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLength: Int = 300,
    isError: Boolean = false,
    supportingText: String? = null,
) {
    val colors = QelloTheme.textFieldColors
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        isError -> colors.line.destructive
        isFocused -> colors.line.focus
        else -> colors.line.default
    }

    val textColor = when {
        isError -> colors.label.destructive
        value.isNotEmpty() -> colors.label.active
        else -> colors.label.default
    }

    val counterColor = if (isError) colors.label.destructive else colors.label.default

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(189.dp)
                .clip(RoundedCornerShape(QelloTheme.radius.radius20))
                .background(colors.container)
                .border(QelloTheme.borderWidth.border1, borderColor, RoundedCornerShape(QelloTheme.radius.radius20))
                .padding(QelloTheme.spacing.spacing20),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                if (value.isEmpty()) {
                    QelloText(text = "텍스트를 입력해주세요.", style = QelloTheme.typography.body2, color = colors.label.default)
                }

                BasicTextField(
                    value = value,
                    onValueChange = { if (it.length <= maxLength) onValueChange(it) },
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    textStyle = QelloTheme.typography.body2.copy(color = textColor),
                    cursorBrush = SolidColor(textColor),
                    interactionSource = interactionSource,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QelloText(
                    text = "(${value.length}/$maxLength)",
                    style = QelloTheme.typography.caption3,
                    color = counterColor,
                )
                if (isError) {
                    // QelloIcon 생기면 실제 경고 아이콘으로 교체
                    Box(
                        modifier = Modifier
                            .size(QelloTheme.iconSize.size24)
                            .clip(CircleShape)
                            .background(QelloTheme.colors.status.destructive),
                    )
                }
            }
        }

        if (isError && supportingText != null) {
            Spacer(Modifier.height(QelloTheme.spacing.spacing8))

            QelloText(
                text = supportingText,
                modifier = Modifier.padding(start = QelloTheme.spacing.spacing20),
                style = QelloTheme.typography.caption2,
                color = colors.label.destructive
            )
        }
    }
}



@Composable
private fun qelloOutlinedTextFieldColors(colors: QelloTextFieldColors) =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = colors.container,
        unfocusedContainerColor = colors.container,
        errorContainerColor = colors.container,
        focusedBorderColor = colors.line.focus,
        unfocusedBorderColor = colors.line.default,
        errorBorderColor = colors.line.destructive,
        focusedTextColor = colors.label.active,
        unfocusedTextColor = colors.label.active,
        errorTextColor = colors.label.destructive,
        cursorColor = colors.label.default,
        errorCursorColor = colors.label.destructive,
    )
