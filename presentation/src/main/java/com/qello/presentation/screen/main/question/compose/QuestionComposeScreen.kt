package com.qello.presentation.screen.main.question.compose

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.qello.presentation.R
import com.qello.presentation.common.rememberSinglePhotoPicker
import com.qello.presentation.designsystem.component.button.QelloBackButton
import com.qello.presentation.designsystem.component.button.QelloLargeButton
import com.qello.presentation.designsystem.component.button.QelloSmallButton
import com.qello.presentation.designsystem.component.item.QuestionCardContent
import com.qello.presentation.designsystem.component.loading.QelloLoadingOverlay
import com.qello.presentation.designsystem.component.spacer.QelloSpacer
import com.qello.presentation.designsystem.component.text.QelloText
import com.qello.presentation.designsystem.component.text.QelloTextArea
import com.qello.presentation.designsystem.theme.QelloColorPalette
import com.qello.presentation.designsystem.theme.QelloSpacing
import com.qello.presentation.designsystem.theme.QelloTheme
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

private const val sideCardScale = 0.838f
private val screenHorizontalPadding = QelloSpacing.spacing20
private val carouselSidePaddingBrowsing = QelloSpacing.spacing72
private val carouselSidePaddingSelected = QelloSpacing.spacing32
private val carouselHeightSelected = 126.dp

@SuppressLint("FrequentlyChangingValue")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionComposeScreen(
    onBack: () -> Unit,
    onNext: (bodyText: String, mediaId: Long?, approvedQuestionId: Long) -> Unit,
    onNavigateToSuggest: () -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
    viewModel: QuestionComposeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuestionComposeSideEffect.ShowSnackbar -> showSnackbar(resources.getString(effect.message))
            }
        }
    }

    var isSelected by remember { mutableStateOf(false) }
    val photoUri = uiState.photoUri
    val content = uiState.content
    val hasContent = photoUri != null || content.isNotBlank()

    val questions = uiState.questions
    val pagerState = key(questions.size) {
        rememberPagerState(
            initialPage = if (questions.isEmpty()) 0 else Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % questions.size,
            pageCount = { if (questions.isEmpty()) 0 else Int.MAX_VALUE },
        )
    }
    val selectedQuestion = if (questions.isEmpty()) null else questions[pagerState.currentPage % questions.size]

    val photoPicker = rememberSinglePhotoPicker(
        onPhotoPicked = viewModel::onPhotoPicked,
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
        QelloSpacer(QelloTheme.spacing.spacing24)

        Box(modifier = Modifier.padding(start = 18.dp)) {
            QelloBackButton(onClick = onBack)
        }

        QelloSpacer(QelloTheme.spacing.spacing28)

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
            val screenWidth = maxWidth

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = maxHeight)
                    .verticalScroll(scrollState)
                    .imePadding(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.padding(horizontal = screenHorizontalPadding)) {
                    QelloSpacer(QelloTheme.spacing.spacing24)

                    AnimatedContent(targetState = isSelected, label = "heading") { selected ->
                        Column(modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing4)) {
                            if (selected) {

                                QelloText(
                                    text = stringResource(R.string.question_compose_write_title_line1),
                                    style = QelloTheme.typography.heading1,
                                    color = QelloTheme.colors.label.strong,
                                )

                                QelloText(
                                    text = stringResource(R.string.question_compose_write_title_line2),
                                    style = QelloTheme.typography.heading1,
                                    color = QelloTheme.colors.label.strong,
                                )

                                QelloSpacer(QelloTheme.spacing.spacing4)

                                QelloText(
                                    text = stringResource(R.string.question_compose_write_caption),
                                    style = QelloTheme.typography.caption1,
                                    color = QelloTheme.colors.primary.normal,
                                )
                            } else {
                                QelloText(
                                    text = stringResource(R.string.question_compose_select_title_line1),
                                    style = QelloTheme.typography.heading1,
                                    color = QelloTheme.colors.label.strong,
                                )
                                QelloText(
                                    text = stringResource(R.string.question_compose_select_title_line2),
                                    style = QelloTheme.typography.heading1,
                                    color = QelloTheme.colors.label.strong,
                                )
                                QelloSpacer(QelloTheme.spacing.spacing4)
                                QelloText(
                                    text = stringResource(R.string.question_compose_select_caption),
                                    style = QelloTheme.typography.caption1,
                                    color = QelloTheme.colors.primary.normal,
                                )
                            }
                        }
                    }
                }

                Column {
                    QelloSpacer(QelloTheme.spacing.spacing32)

                    // 질문을 고르면 카드가 가로로 넓어지도록 좌우 여백을 줄인다
                    val carouselSidePadding by animateDpAsState(
                        targetValue = if (isSelected) carouselSidePaddingSelected else carouselSidePaddingBrowsing,
                        label = "carouselSidePadding",
                    )
                    val carouselHeight by animateDpAsState(
                        // 둘러보는 동안에는 카드가 정사각형이 되도록 높이를 카드 너비와 같게 둔다
                        targetValue = if (isSelected) {
                            carouselHeightSelected
                        } else {
                            screenWidth - carouselSidePaddingBrowsing * 2
                        },
                        label = "carouselHeight",
                    )

                    if (questions.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(carouselHeight),
                        ) {
                            if (uiState.isLoadingQuestions) {
                                QelloLoadingOverlay()
                            }
                        }
                    } else {
                        HorizontalPager(
                            state = pagerState,
                            pageSize = PageSize.Fill,
                            contentPadding = PaddingValues(horizontal = carouselSidePadding),
                            pageSpacing = QelloTheme.spacing.spacing10,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(carouselHeight),
                        ) { page ->
                            val question = questions[page % questions.size]

                            // 가운데 카드보다 왼쪽에 있으면 양수, 오른쪽에 있으면 음수
                            val signedPageOffset =
                                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                            val pageOffset = signedPageOffset.absoluteValue

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        // 가운데에서 멀어질수록 카드를 조금 작게 보여서 선택된 카드가 돋보이게 한다
                                        val scale = 1f - (1f - sideCardScale) * pageOffset.coerceIn(0f, 1f)
                                        scaleX = scale
                                        scaleY = scale
                                        // 줄어든 만큼 가운데 쪽으로 붙여서, 카드 사이 간격이 pageSpacing 그대로 보이게 한다
                                        translationX = signedPageOffset.coerceIn(-1f, 1f) * size.width * (1f - sideCardScale) / 2f
                                    }
                                    .clip(RoundedCornerShape(QelloTheme.radius.radius20))
                                    .background(QelloColorPalette.Navy20)
                                    .clickable { isSelected = true },
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer { alpha = 1f - pageOffset.coerceIn(0f, 1f) }
                                        .background(QelloTheme.gradient.carouselCardHighlight),
                                )

                                QuestionCardContent(
                                    questionText = question.questionText,
                                    textColor = lerp(
                                        QelloTheme.colors.label.strong,
                                        QelloColorPalette.Navy60,
                                        pageOffset.coerceIn(0f, 1f),
                                    ),
                                    isSelected = isSelected,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }

                        // 질문을 고르고 나면 사진/글 입력 영역이 올라오므로, 둘러보는 동안에만 몇 번째인지 보여준다
                        AnimatedVisibility(
                            visible = !isSelected,
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = QelloTheme.spacing.spacing12),
                                contentAlignment = Alignment.Center,
                            ) {
                                QelloText(
                                    text = stringResource(
                                        R.string.question_compose_page_indicator,
                                        pagerState.currentPage % questions.size + 1,
                                        questions.size,
                                    ),
                                    style = QelloTheme.typography.caption3,
                                    color = QelloColorPalette.Navy80,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(QelloTheme.radius.radiusFull))
                                        .background(QelloColorPalette.Navy30)
                                        .padding(
                                            horizontal = QelloTheme.spacing.spacing12,
                                            vertical = QelloTheme.spacing.spacing4,
                                        ),
                                )
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }),
                    ) {
                        Column(modifier = Modifier.bringIntoViewRequester(photoSectionBringIntoViewRequester)) {
                            QelloSpacer(QelloTheme.spacing.spacing20)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(Color.Black.copy(alpha = 0.4f)),
                            )

                            Column(modifier = Modifier.padding(horizontal = screenHorizontalPadding)) {
                                QelloSpacer(QelloTheme.spacing.spacing20)

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp)
                                        .clip(RoundedCornerShape(QelloTheme.radius.radius20)),
                                ) {
                                    if (photoUri != null) {
                                        AsyncImage(
                                            model = photoUri,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clickable(enabled = !uiState.isUploadingPhoto) { photoPicker.launch() },
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
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

                                    if (uiState.isUploadingPhoto) {
                                        QelloLoadingOverlay()
                                    }
                                }

                                QelloSpacer(QelloTheme.spacing.spacing12)

                                QelloTextArea(
                                    value = content,
                                    onValueChange = viewModel::onContentChanged,
                                    placeholder = stringResource(R.string.question_compose_content_placeholder),
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = screenHorizontalPadding)) {
                    QelloSpacer(QelloTheme.spacing.spacing20)

                    if (isSelected && hasContent && selectedQuestion != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = QelloTheme.spacing.spacing24),
                            horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
                        ) {
                            QelloSmallButton(
                                text = stringResource(R.string.question_compose_go_suggest_button),
                                modifier = Modifier.weight(1f),
                                colors = QelloTheme.buttonColors.darkButtonColors,
                                onClick = onNavigateToSuggest,
                            )
                            QelloSmallButton(
                                text = stringResource(R.string.question_compose_go_direction_button),
                                modifier = Modifier.weight(1f),
                                onClick = { onNext(content, uiState.uploadedMediaId, selectedQuestion.approvedQuestionId) },
                            )
                        }
                    } else {
                        QelloLargeButton(
                            text = stringResource(R.string.question_compose_go_suggest_button),
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
