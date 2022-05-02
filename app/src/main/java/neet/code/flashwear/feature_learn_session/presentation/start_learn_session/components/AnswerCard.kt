package neet.code.flashwear.feature_learn_session.presentation.start_learn_session.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.StartLearnSessionState


@Composable
fun AnswerCard(
    startLearnSessionState: StartLearnSessionState,
    sizeTitle: MutableState<TextUnit>,
    sizeContent: MutableState<TextUnit>,
    sizeSub: MutableState<TextUnit>
) {
    if(startLearnSessionState.showAnswer and startLearnSessionState.currentQuestionsBatch.isNotEmpty()) {
        Surface(
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(!startLearnSessionState.currentQuestion?.answerTitle.isNullOrBlank()) {
                    startLearnSessionState.currentQuestion?.answerTitle?.let { answerTitle ->
                            Card(
                                text = answerTitle,
                                maxLines = 2,
                                paddingValues = PaddingValues(top = 8.dp),
                                size = sizeTitle
                            )
                        }
                    }
                if(!startLearnSessionState.currentQuestion?.answerSub.isNullOrBlank()) {
                    startLearnSessionState.currentQuestion?.answerSub?.let { answerSub ->
                        Card(
                            text = answerSub,
                            maxLines = 2,
                            paddingValues = PaddingValues(bottom = 15.dp),
                            size = sizeSub,
                        )
                    }
                }
                if(!startLearnSessionState.currentQuestion?.answerContent.isNullOrBlank()) {
                    startLearnSessionState.currentQuestion?.answerContent?.let { answerContent ->
                        Card(
                            text = answerContent,
                            maxLines = 3,
                            paddingValues = PaddingValues(bottom = 8.dp),
                            size = sizeContent
                        )
                    }
                }
            }
        }
    }
}

