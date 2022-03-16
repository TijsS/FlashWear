package neet.code.flashwear.feature_deck.presentation.view_deck.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Row(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    if (viewModel.viewDeckState.value.questionIsHeldForDelete) {
                        viewModel.onEvent(ViewDeckEvent.ToggleDeleteQuestion)
                    } else {
                        navController.navigate(Screen.ProgressScreen.route)
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
            question.questionTitle?.let { DeckQuestion(it) }

            TabRowDefaults.Divider(
                color = Color.Red,
                thickness = 1.dp
            )

            if(!question.answerTitle.isNullOrBlank()){
                DeckAnswer(question.answerTitle!!)
            }

            Spacer(modifier = Modifier.size(3.dp))

            if(!question.answerContent.isNullOrBlank()){
                DeckAnswer(question.answerContent!!)
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
            Text(
                text = "Hello test",
                style = MaterialTheme.typography.overline
            )
            question.score?.let { CircularProgressAnimated(it) }
        }
    }
}
