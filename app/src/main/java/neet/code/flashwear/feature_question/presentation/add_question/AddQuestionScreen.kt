package neet.code.flashwear.feature_question.presentation.add_question

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import neet.code.flashwear.R
import neet.code.flashwear.core.presentation.components.FlashWearDrawer
import neet.code.flashwear.core.presentation.components.FlashWearTopBar
import neet.code.flashwear.core.presentation.components.TransparentHintTextField


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddQuestionScreen(
    navController: NavController,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    viewModel: AddQuestionViewModel = hiltViewModel()
){
    val scope = rememberCoroutineScope()
    val questionState = viewModel.questionState.value
    val scaffoldState = rememberScaffoldState()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddQuestionViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        showSnackbar(context.getString(event.baseMessage).replace("###", event.message)
                            , SnackbarDuration.Short
                        )
                    }
                }
                is AddQuestionViewModel.UiEvent.SaveQuestion -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddQuestionEvent.AddQuestion)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = stringResource(R.string.save_question))
            }
        },
        topBar = {
            FlashWearTopBar(
                state = scaffoldState,
                scope = scope,
                title = stringResource(R.string.update_question),
                withFunction = false,
            )
        },
        drawerContent = {
            FlashWearDrawer(
                navController = navController,
                scope = scope
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            TransparentHintTextField(
                text = questionState.questionTitle,
                label = stringResource(R.string.question_title),
                onValueChange = {
                    viewModel.onEvent(AddQuestionEvent.EnteredQuestionTitle(it))
                },
                singleLine = false,
                showSnackbar = showSnackbar,
                textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = MaterialTheme.typography.body1.fontSize))
            )

            Spacer(modifier = Modifier.height(4.dp))

            TransparentHintTextField(
                text = questionState.questionContent,
                label = stringResource(R.string.question_content),
                onValueChange = {
                    viewModel.onEvent(AddQuestionEvent.EnteredQuestionContent(it))
                },
                singleLine = false,
                showSnackbar = showSnackbar,
                textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = MaterialTheme.typography.body1.fontSize))
            )


            Spacer(modifier = Modifier.height(8.dp))

            TransparentHintTextField(
                text = questionState.answerTitle,
                label = stringResource(R.string.answer_Title),
                onValueChange = {
                    viewModel.onEvent(AddQuestionEvent.EnteredAnswerTitle(it))
                },
                singleLine = false,
                showSnackbar = showSnackbar,
                textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = MaterialTheme.typography.body1.fontSize))
            )


            Spacer(modifier = Modifier.height(4.dp))

            TransparentHintTextField(
                text = questionState.answerContent,
                label = stringResource(R.string.answer_content),
                onValueChange = {
                    viewModel.onEvent(AddQuestionEvent.EnteredAnswerContent(it))
                },
                singleLine = false,
                showSnackbar = showSnackbar,
                textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = MaterialTheme.typography.body1.fontSize))
            )


            Spacer(modifier = Modifier.height(4.dp))

            TransparentHintTextField(
                text = questionState.answerSub,
                maxChars = 100,
                maxLines = 2,
                label = stringResource(R.string.answer_sub),
                onValueChange = {
                    viewModel.onEvent(AddQuestionEvent.EnteredAnswerSub(it))
                },
                singleLine = false,
                showSnackbar = showSnackbar,
                textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = MaterialTheme.typography.body1.fontSize)),
            )
        }
    }
}