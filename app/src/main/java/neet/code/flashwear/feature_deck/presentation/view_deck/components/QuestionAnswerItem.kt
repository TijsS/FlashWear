package neet.code.flashwear.feature_deck.presentation.view_deck.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import neet.code.flashwear.Screen
import neet.code.flashwear.feature_deck.presentation.view_deck.*
import neet.code.flashwear.feature_question.domain.model.Question

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionAnswerItem(
    question: Question,
    navController: NavController,
    viewModel: ViewDeckViewModel = hiltViewModel()
){
    var questionBigSizeSet = false
    var answerBigSizeSet = false
    val bigFontSize = MaterialTheme.typography.subtitle1.fontSize
    val smallFontSize = MaterialTheme.typography.body2.fontSize

    Row(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    if (viewModel.viewDeckState.value.questionIsHeldForDelete) {
                        viewModel.onEvent(ViewDeckEvent.ToggleDeleteQuestion)
                    } else {
                        navController.navigate("${Screen.ViewQuestionScreen.route}?questionId=${question.id}")
                    }
                },
                onLongClick = {
                    viewModel.onEvent(ViewDeckEvent.ToggleDeleteQuestion)
                }
            )
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(3f)
                .align(Alignment.CenterVertically)
        ) {
            if(!question.questionTitle.isNullOrBlank()){
                DeckItemText(
                    question.questionTitle!!,
                    fontSize = bigFontSize
                )
                questionBigSizeSet = true
            }

            if(!question.questionContent.isNullOrBlank()){
                DeckItemText(
                    question.questionContent!!,
                    fontSize = if (questionBigSizeSet) smallFontSize else bigFontSize
                )
            }

            TabRowDefaults.Divider(
                color = Color.Red,
                thickness = 1.dp
            )

            if(!question.answerTitle.isNullOrBlank()){
                DeckItemText(
                    question.answerTitle!!,
                    fontSize = bigFontSize
                )
                answerBigSizeSet = true
            }

            Spacer(modifier = Modifier.size(3.dp))

            if(!question.answerContent.isNullOrBlank()){
                DeckItemText(
                    question.answerContent!!,
                    fontSize = if (answerBigSizeSet) smallFontSize else bigFontSize
                )
            }
        }

        Divider(
            color = Color.Red,
            modifier = Modifier
                .height(70.dp)
                .width(1.dp)
        )

        Column(
            Modifier.padding(5.dp)
        ) {
            question.score.let { CircularProgressAnimated(it) }
        }
    }
}

@Composable
fun CircularProgressAnimated(score: Double) {

    Surface(
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .padding(3.dp),
        color = MaterialTheme.colors.primary.copy(alpha = ProgressIndicatorDefaults.IndicatorBackgroundOpacity)
    ) {
        CircularProgressIndicator(
            progress = if (score == 0.1) 0.01f else score.toFloat(),
        )
    }
}

@Composable
fun DeckItemText(answer: String, fontSize: TextUnit) {
    Text(answer,
        maxLines = 1,
        modifier = Modifier
            .requiredHeightIn(max = 60.dp),
        overflow = TextOverflow.Ellipsis,
        fontSize = fontSize
    )
}

