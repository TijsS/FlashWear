package neet.code.flashwear.feature_learn_session.presentation.start_learn_session.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
fun QuestionCard(
    startLearnSessionState: StartLearnSessionState,
    sizeTitle: MutableState<TextUnit>,
    sizeContent: MutableState<TextUnit>,
    sizeSub: MutableState<TextUnit>,
    ) {

    Surface(
        color = MaterialTheme.colors.onSecondary,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {

        //when list is filled
        AnimatedVisibility(
            visible = startLearnSessionState.currentQuestionsBatch.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                //center the title if Question doesnt containt content
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(!startLearnSessionState.currentQuestion?.questionTitle.isNullOrBlank())
                {
                    startLearnSessionState.currentQuestion?.questionTitle?.let { questionTitle ->
                        Card(
                            text = questionTitle,
                            maxLines = 2,
                            textStyleBody = MaterialTheme.typography.h3.fontSize,
                            paddingValues = PaddingValues(top = 8.dp, bottom = 15.dp),
                            size = sizeTitle,
                        )
                    }
                }

                if(!startLearnSessionState.currentQuestion?.questionContent.isNullOrBlank())
                {
                    startLearnSessionState.currentQuestion?.questionContent?.let { questionContent ->
                        Card(
                            text = questionContent,
                            maxLines = 3,
                            textStyleBody = MaterialTheme.typography.h6.fontSize,
                            paddingValues = PaddingValues(bottom = 8.dp),
                            size = sizeContent
                        )
                    }
                }
            }
        }
    }
}