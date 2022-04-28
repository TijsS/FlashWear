package neet.code.wearable.feature_question.presentation

import android.content.res.Configuration
import android.view.Surface
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.*
import androidx.wear.compose.material.R
import androidx.wear.widget.BoxInsetLayout
import kotlinx.coroutines.launch
import neet.code.wearable.Screen
import neet.code.wearable.core.presentation.CompactChipButton
import neet.code.wearable.feature_deck.presentation.LearnStyle
import neet.code.wearable.feature_question.domain.Question
import neet.code.wearable.theme.FlashWearTheme

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun QuestionScreen(
    navController: NavController,
    viewModel: QuestionViewModel = hiltViewModel(),

){
    val question = viewModel.question.value.currentQuestion
    val scalingLazyListStateAnswer = rememberScalingLazyListState()
    val scalingLazyListStateQuestion = rememberScalingLazyListState()

    val questionstate = viewModel.question.value
    if(question != null) {
        if (!questionstate.showAnswer) {
            ScalingLazyColumn(
                state = scalingLazyListStateQuestion,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                item { Spacer(modifier = Modifier.padding(top = if(question.questionContent.isNullOrBlank() && question.questionTitle?.length!! < 6) 30.dp else 80.dp)) }

                if (!question.questionTitle.isNullOrBlank()) {
                    item {
                        question.questionTitle?.let { questionTitle ->
                            Text(
                                text = questionTitle,
                                textAlign = TextAlign.Center,
                                fontStyle = MaterialTheme.typography.title2.fontStyle,
                                fontSize = if(question.questionContent.isNullOrBlank() && questionTitle.length < 6) 40.sp else if(questionTitle.length < 12 )25.sp else 15.sp
                            )
                        }
                    }
                }

                if (!question.questionContent.isNullOrBlank()) {
                    item {
                        question.questionContent?.let { Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            fontSize = if(question.answerTitle.isNullOrEmpty() && !question.answerContent!!.contains(" ") && question.answerContent!!.length < 12) 48.sp else 14.sp
                        ) }
                    }
                }

                item {
                    CompactChip(
                        onClick = {
                            viewModel.onEvent(QuestionEvent.ShowAnswer)
                        },
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth(),
                        label = { Text(text = "show", textAlign = TextAlign.Center) },
                        icon = {
                            Icon(
                                painter = painterResource(neet.code.wearable.R.drawable.show),
                                contentDescription = "play icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .wrapContentSize(align = Alignment.Center),
                            )
                        },
                    )
                }
                item { Spacer(modifier = Modifier.padding(top = 30.dp)) }
            }
        } else {
            ScalingLazyColumn(
                state = scalingLazyListStateAnswer,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item { Spacer(modifier = Modifier.padding(top = 80.dp)) }

                if (!question.questionTitle.isNullOrBlank()){
                    item {
                        question.questionTitle?.let {
                            Text(
                                text = it,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 3.dp),
                                fontStyle = MaterialTheme.typography.caption2.fontStyle,
                                fontSize = MaterialTheme.typography.caption2.fontSize
                            )
                        }
                    }
                }else{
                    item {
                        question.questionContent?.let {
                            Text(
                                text = it,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 3.dp),
                                fontStyle = MaterialTheme.typography.caption2.fontStyle,
                                fontSize = MaterialTheme.typography.caption2.fontSize
                            )
                        }
                    }
                }


                if (!question.answerTitle.isNullOrBlank()){
                    item {
                        question.answerTitle?.let {
                            Text(
                                text = it,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 1.dp),
                                fontStyle = MaterialTheme.typography.title2.fontStyle,
                                fontSize = MaterialTheme.typography.title2.fontSize
                            )
                        }
                    }
                }

                if (!question.answerSub.isNullOrBlank()) {
                    item {
                        question.answerSub?.let { Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 3.dp),
                            fontStyle = MaterialTheme.typography.caption2.fontStyle,
                            fontSize = MaterialTheme.typography.caption2.fontSize
                        ) }
                    }
                }

                if (!question.answerContent.isNullOrBlank()) {
                    item {
                        question.answerContent?.let {
                            Text(
                                text = it,
                                textAlign = TextAlign.Center,
                                fontStyle = MaterialTheme.typography.title2.fontStyle,
                                fontSize = MaterialTheme.typography.title2.fontSize
                            )
                        }
                    }
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.padding(top = 15.dp)
                    ) {

                        CompactChipButton(
                            drawable = neet.code.wearable.R.drawable.wrong,
                            colors = ChipDefaults.chipColors(),
                            onClick = { viewModel.onEvent(QuestionEvent.NextQuestion(value = "-")) }
                        )

                        CompactChipButton(
                            drawable = neet.code.wearable.R.drawable.half_correct,
                            colors = ChipDefaults.chipColors(),
                            onClick = { viewModel.onEvent(QuestionEvent.NextQuestion(value = "=")) }
                        )

                        CompactChipButton(
                            drawable = neet.code.wearable.R.drawable.correct,
                            colors = ChipDefaults.chipColors(),
                            onClick = { viewModel.onEvent(QuestionEvent.NextQuestion(value = "+")) }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.padding(top = 30.dp)) }
            }
        }
    }

    ReturnTrigger(viewModel, navController)
}

@Composable
fun ReturnTrigger(viewModel: QuestionViewModel, navController: NavController) {
    if(viewModel.question.value.finished){
        navController.navigate(
            Screen.DecksScreen.route
        )
        viewModel.question.value.finished = false
    }
}