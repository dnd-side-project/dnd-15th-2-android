package com.qello.presentation.navigation.main

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.qello.presentation.R
import kotlinx.serialization.Serializable

@Serializable
enum class QuestionCompleteType {
    SEND_QUESTION,
    SUGGEST_QUESTION,
}

data class QuestionCompleteContent(
    val titleLine1: String,
    val titleLine2: String,
    val caption: String,
    @DrawableRes val imageRes: Int,
    val primaryButtonText: String,
    val secondaryButtonText: String,
    // null이면 홈으로 이동한다
    val primaryDestination: MainNavKey?,
    val secondaryDestination: MainNavKey?,
)

@Composable
fun QuestionCompleteType.toContent(): QuestionCompleteContent = when (this) {
    QuestionCompleteType.SEND_QUESTION -> QuestionCompleteContent(
        titleLine1 = "동쪽으로 질문을 보냈어요!",
        titleLine2 = "곧 답변이 도착할 거예요",
        caption = "켈로에서 많은 사람들과 질문하며 알아가요",
        imageRes = R.drawable.img_flight,
        primaryButtonText = "내 질문 보러 가기",
        secondaryButtonText = stringResource(R.string.navigate_home_button),
        primaryDestination = MainNavKey.SentQuestionList,
        secondaryDestination = null,
    )

    QuestionCompleteType.SUGGEST_QUESTION -> QuestionCompleteContent(
        titleLine1 = "질문 제안을 보냈어요!",
        titleLine2 = "곧 검토가 진행될 거예요",
        caption = "검토가 완료되면 알림을 드릴게요!",
        imageRes = R.drawable.img_file,
        primaryButtonText = stringResource(R.string.question_suggest_go_home_button),
        secondaryButtonText = stringResource(R.string.question_suggest_go_send_button),
        primaryDestination = null,
        secondaryDestination = MainNavKey.QuestionCompose,
    )
}
