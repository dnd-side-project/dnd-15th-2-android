package com.qello.presentation.ui.screen.main.question

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloBackButton
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.button.QelloSmallButton
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.component.text.QelloTextArea
import com.qello.presentation.media.rememberSinglePhotoPicker
import com.qello.presentation.ui.designsystem.QelloColorPalette
import com.qello.presentation.ui.designsystem.theme.QelloTheme
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

private data class QuestionSuggestion(
    val id: Int,
    val text: String,
)

private val mockQuestions = listOf(
    QuestionSuggestion(1, "다들 어떤 스포츠\n좋아하시나요?"),
    QuestionSuggestion(2, "요즘 제일 자주\n듣는 노래는요?"),
    QuestionSuggestion(3, "주말엔 보통\n뭐 하시나요?"),
)

private val screenHorizontalPadding = 20.dp
private val carouselHeightBrowsing = 320.dp
private val carouselHeightSelected = 126.dp
private val avatarSizeBrowsing = 160.dp
private val avatarSizeSelected = 80.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionComposeScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    onNavigateToSuggest: () -> Unit,
) {
    var isSelected by remember { mutableStateOf(false) }
    var photoUri by remember { mutableStateOf<String?>(null) }
    var content by remember { mutableStateOf("") }
    val hasContent = photoUri != null || content.isNotBlank()

    val photoPicker = rememberSinglePhotoPicker(
        onPhotoPicked = { uri -> photoUri = uri },
    )

    val progressFraction by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.3f,
        label = "progressFraction",
    )

    val photoSectionBringIntoViewRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(isSelected) {
        if (isSelected) {
            delay(150)
            photoSectionBringIntoViewRequester.bringIntoView()
        }
    }

    val scrollState = rememberScrollState()
    val imeVisible = WindowInsets.isImeVisible

    LaunchedEffect(imeVisible) {
        if (imeVisible) {
            repeat(8) {
                scrollState.animateScrollTo(scrollState.maxValue)
                delay(50)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QelloTheme.gradient.backgroundStrong),
    ) {
        Spacer(Modifier.height(24.dp))

        Box(modifier = Modifier.padding(start = 18.dp)) {
            QelloBackButton(onClick = onBack)
        }

        Spacer(Modifier.height(29.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(QelloTheme.colors.line.normal),
        ) {
            val fillBrush = remember(progressFraction) {
                val progressStop = progressFraction.coerceIn(0f, 0.97f)
                Brush.horizontalGradient(
                    0f to QelloColorPalette.Bule30,
                    progressStop to QelloColorPalette.Bule30,
                    1f to QelloColorPalette.Neutral0,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(fillBrush),
            )
        }

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = maxHeight)
                    .verticalScroll(scrollState)
                    .imePadding(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.padding(horizontal = screenHorizontalPadding)) {
                    Spacer(Modifier.height(QelloTheme.spacing.spacing24))

                    AnimatedContent(targetState = isSelected, label = "heading") { selected ->
                        Column(modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing4)) {
                            if (selected) {
                                QelloText(
                                    text = "사진 혹은",
                                    style = QelloTheme.typography.heading1,
                                    color = QelloTheme.colors.label.strong,
                                )
                                QelloText(
                                    text = "글을 작성해보세요!",
                                    style = QelloTheme.typography.heading1,
                                    color = QelloTheme.colors.label.strong,
                                )
                                Spacer(Modifier.height(QelloTheme.spacing.spacing4))
                                QelloText(
                                    text = "사진 1장으로도 충분해요!",
                                    style = QelloTheme.typography.caption1,
                                    color = QelloTheme.colors.primary.normal,
                                )
                            } else {
                                QelloText(text = "좋아요!", style = QelloTheme.typography.heading1, color = QelloTheme.colors.label.strong)
                                QelloText(
                                    text = "질문을 선택해볼까요?",
                                    style = QelloTheme.typography.heading1,
                                    color = QelloTheme.colors.label.strong,
                                )
                                Spacer(Modifier.height(QelloTheme.spacing.spacing4))
                                QelloText(
                                    text = "마음에 드는 질문을 골라 보내보세요.",
                                    style = QelloTheme.typography.caption1,
                                    color = QelloTheme.colors.primary.normal,
                                )
                            }
                        }
                    }
                }

                Column {
                    Spacer(Modifier.height(QelloTheme.spacing.spacing32))

                    val pagerState = rememberPagerState(
                        initialPage = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % mockQuestions.size,
                        pageCount = { Int.MAX_VALUE },
                    )
                    val carouselHeight by animateDpAsState(
                        targetValue = if (isSelected) carouselHeightSelected else carouselHeightBrowsing,
                        label = "carouselHeight",
                    )
                    val avatarSize by animateDpAsState(
                        targetValue = if (isSelected) avatarSizeSelected else avatarSizeBrowsing,
                        label = "avatarSize",
                    )

                    HorizontalPager(
                        state = pagerState,
                        pageSize = PageSize.Fill,
                        contentPadding = PaddingValues(horizontal = QelloTheme.spacing.spacing32),
                        pageSpacing = QelloTheme.spacing.spacing20,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(carouselHeight),
                    ) { page ->
                        val question = mockQuestions[page % mockQuestions.size]

                        val pageOffset =
                            ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(QelloTheme.radius.radius20))
                                .background(QelloTheme.colors.imagefield.default)
                                .clickable { isSelected = true },
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer { alpha = 1f - pageOffset.coerceIn(0f, 1f) }
                                    .background(QelloTheme.gradient.cardHighlight),
                            )
                            QelloText(
                                text = question.text,
                                style = QelloTheme.typography.heading2,
                                color = QelloTheme.colors.label.strong,
                                modifier = Modifier
                                    .align(if (isSelected) Alignment.CenterStart else Alignment.TopStart)
                                    .padding(QelloTheme.spacing.spacing32),
                            )
                            Box(
                                modifier = Modifier
                                    .align(if (isSelected) Alignment.CenterEnd else Alignment.BottomEnd)
                                    .padding(QelloTheme.spacing.spacing32)
                                    .size(avatarSize)
                                    .clip(CircleShape)
                                    .background(QelloTheme.colors.status.destructive),
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }),
                    ) {
                        Column(modifier = Modifier.bringIntoViewRequester(photoSectionBringIntoViewRequester)) {
                            Spacer(Modifier.height(QelloTheme.spacing.spacing20))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(Color.Black.copy(alpha = 0.4f)),
                            )

                            Column(modifier = Modifier.padding(horizontal = screenHorizontalPadding)) {
                                Spacer(Modifier.height(QelloTheme.spacing.spacing20))

                                if (photoUri != null) {
                                    AsyncImage(
                                        model = photoUri,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(160.dp)
                                            .clip(RoundedCornerShape(QelloTheme.radius.radius20))
                                            .clickable { photoPicker.launch() },
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(160.dp)
                                            .clip(RoundedCornerShape(QelloTheme.radius.radius20))
                                            .background(QelloTheme.colors.imagefield.default)
                                            .clickable { photoPicker.launch() },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_picture),
                                            contentDescription = null,
                                            modifier = Modifier.size(QelloTheme.iconSize.size48),
                                            tint = QelloColorPalette.Navy30,
                                        )
                                    }
                                }

                                Spacer(Modifier.height(QelloTheme.spacing.spacing12))

                                QelloTextArea(
                                    value = content,
                                    onValueChange = { content = it },
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = screenHorizontalPadding)) {
                    Spacer(Modifier.height(QelloTheme.spacing.spacing20))

                    if (isSelected && hasContent) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = QelloTheme.spacing.spacing24),
                            horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
                        ) {
                            QelloSmallButton(
                                text = "질문 제안하러 가기",
                                modifier = Modifier.weight(1f),
                                colors = QelloTheme.buttonColors.darkButtonColors,
                                onClick = onNavigateToSuggest,
                            )
                            QelloSmallButton(
                                text = "방향 설정하러 가기",
                                modifier = Modifier.weight(1f),
                                onClick = onNext,
                            )
                        }
                    } else {
                        QelloLargeButton(
                            text = "질문 제안하러 가기",
                            colors = QelloTheme.buttonColors.darkButtonColors,
                            modifier = Modifier.padding(bottom = QelloTheme.spacing.spacing24),
                            onClick = onNavigateToSuggest,
                        )
                    }
                }
            }
        }
    }
}
