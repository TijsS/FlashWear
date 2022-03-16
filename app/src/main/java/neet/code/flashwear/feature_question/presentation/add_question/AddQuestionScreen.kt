package neet.code.flashwear.feature_question.presentation.add_question

import neet.code.flashwear.feature_question.presentation.add_question.AddQuestionEvent
import neet.code.flashwear.feature_question.presentation.add_question.AddQuestionViewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import neet.code.flashwear.core.presentation.components.FlashWearDrawer
import neet.code.flashwear.core.presentation.components.FlashWearTopBar
import neet.code.flashwear.feature_deck.presentation.add_deck.components.TransparentHintTextField


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddQuestionScreen(
    navController: NavController,
    viewModel: AddQuestionViewModel = hiltViewModel()
){
    val scope = rememberCoroutineScope()
    val questionState = viewModel.questionState.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddQuestionViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
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
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save question")
            }
        },
        topBar = {
            FlashWearTopBar(
                state = scaffoldState,
                scope = scope,
                title = "Add question",
                withFunction = false,
            )
        },
        drawerContent = { FlashWearDrawer(
            navController = navController,
            scaffoldState = scaffoldState,
            scope = scope
        ) },
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
                label = "Question Title",
                onValueChange = {
                    viewModel.onEvent(AddQuestionEvent.EnteredQuestionTitle(it))
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary))
            )

            Spacer(modifier = Modifier.height(4.dp))

            TransparentHintTextField(
                text = questionState.questionContent,
                label = "Question Content",
                onValueChange = {
                    viewModel.onEvent(AddQuestionEvent.EnteredQuestionContent(it))
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary))
            )


            Spacer(modifier = Modifier.height(8.dp))

            TransparentHintTextField(
                text = questionState.answerTitle,
                label = "Answer Title",
                onValueChange = {
                    viewModel.onEvent(AddQuestionEvent.EnteredAnswerTitle(it))
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary))
            )


            Spacer(modifier = Modifier.height(4.dp))

            TransparentHintTextField(
                text = questionState.answerContent,
                label = "Answer Content",
                onValueChange = {
                    viewModel.onEvent(AddQuestionEvent.EnteredAnswerContent(it))
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary))
            )


            Spacer(modifier = Modifier.height(4.dp))

            TransparentHintTextField(
                text = questionState.answerSub,
                label = "Answer Subtitle",
                onValueChange = {
                    viewModel.onEvent(AddQuestionEvent.EnteredAnswerSub(it))
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary))
            )

        }
    }
}