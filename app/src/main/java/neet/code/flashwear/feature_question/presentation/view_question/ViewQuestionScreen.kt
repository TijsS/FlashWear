package neet.code.flashwear.feature_question.presentation.view_question

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import neet.code.flashwear.R
import neet.code.flashwear.core.presentation.components.*
import neet.code.flashwear.feature_deck.presentation.add_deck.AddDeckViewModel
import neet.code.flashwear.feature_deck.presentation.add_deck.components.TransparentHintTextField
import neet.code.flashwear.feature_question.presentation.add_question.AddQuestionEvent

@Composable
fun ViewQuestionScreen (
    navController: NavController,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    viewModel: ViewQuestionViewModel = hiltViewModel()
){
    val scope = rememberCoroutineScope()
    val questionState = viewModel.questionState.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is ViewQuestionViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        showSnackbar(event.message, SnackbarDuration.Short)
                    }
                }
                is ViewQuestionViewModel.UiEvent.UpdateQuestion -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(ViewQuestionEvent.UpdateQuestion)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = stringResource(R.string.save_question))
            }
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        topBar = {
            FlashWearTopBar(
                state = scaffoldState,
                scope = scope,
                title = stringResource(R.string.add_question),
                withFunction = false,
            )
        },
        drawerContent = {
            FlashWearDrawer(
                navController = navController,
                scope = scope
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                questionState.question?.questionTitle?.let { questionTitle ->
                    TransparentHintTextField(
                        text = questionTitle,
                        label = stringResource(R.string.question_title),
                        onValueChange = {
                            viewModel.onEvent(ViewQuestionEvent.EnteredQuestionTitle(it))
                        },
                        singleLine = false,
                        textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = MaterialTheme.typography.body1.fontSize))
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                questionState.question?.questionContent?.let { questionContent ->
                    TransparentHintTextField(
                        text = questionContent,
                        label = stringResource(R.string.question_content),
                        onValueChange = {
                            viewModel.onEvent(ViewQuestionEvent.EnteredQuestionContent(it))
                        },
                        singleLine = false,
                        textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = MaterialTheme.typography.body1.fontSize))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                questionState.question?.answerTitle?.let { answerTitle ->
                    TransparentHintTextField(
                        text = answerTitle,
                        label = stringResource(R.string.answer_Title),
                        onValueChange = {
                            viewModel.onEvent(ViewQuestionEvent.EnteredAnswerTitle(it))
                        },
                        singleLine = false,
                        textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = MaterialTheme.typography.body1.fontSize))
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                questionState.question?.answerContent?.let { answerContent ->
                    TransparentHintTextField(
                        text = answerContent,
                        label = stringResource(R.string.answer_content),
                        onValueChange = {
                            viewModel.onEvent(ViewQuestionEvent.EnteredAnswerContent(it))
                        },
                        singleLine = false,
                        textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = MaterialTheme.typography.body1.fontSize))
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                questionState.question?.answerSub?.let { answerSub ->
                    TransparentHintTextField(
                        text = answerSub,
                        label = stringResource(R.string.answer_sub),
                        onValueChange = {
                            viewModel.onEvent(ViewQuestionEvent.EnteredAnswerSub(it))
                        },
                        singleLine = false,
                        textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = MaterialTheme.typography.body1.fontSize))
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                TransparentHintTextField(
                    text = questionState.question?.score.toString(),
                    label = stringResource(R.string.score),
                    onValueChange = {
                        viewModel.onEvent(ViewQuestionEvent.EnteredScore(it))
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = MaterialTheme.typography.body1.fontSize)),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
                )
            }
        }
    }
}