package neet.code.flashwear.feature_deck.presentation.view_deck.components

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.launch
import neet.code.flashwear.Screen
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckEvent
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckState
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckViewModel
import neet.code.flashwear.feature_question.domain.model.Question

@Composable
fun FloatingMenuButton(viewModel: ViewDeckViewModel, viewDeckState: ViewDeckState, navController: NavController) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val pickFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { csv ->
        if (csv != null) {
            val importedQuestions: MutableList<Question> = mutableListOf()

            context.contentResolver.openInputStream(csv)?.use { inputStream ->
                csvReader().open(inputStream){
                    readAllWithHeaderAsSequence().forEach { row ->
                        Log.i(TAG, "question: $row ")
                        Log.i(TAG, "questionTitle: ${row["questionTitle"]} ")

                        val question = Question(
                            questionTitle = row["QuestionTitle"],
                            questionContent = row["QuestionContent"],
                            answerTitle = row["AnswerTitle"],
                            answerContent = row["AnswerContent"],
                            answerSub = row["answerSub"],
                            deckId = viewDeckState.deckId
                        )
                        importedQuestions.add(question)
                    }
                }
            }

            viewModel.onEvent(ViewDeckEvent.ImportQuestions(importedQuestions))

            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = viewDeckState.importedQuestionsMessage
                )
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = viewDeckState.isFloatingMenuVisible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column {

                FloatingMenuButtonItem(
                    navController = navController,
                    viewDeckState = viewDeckState,
                    onClick = { navController.navigate("${Screen.AddQuestionScreen.route}?deckId=${viewDeckState.deckId}") },
                    text = "Add",
                    imageVector = Icons.Filled.Add
                )

                Spacer(modifier = Modifier.size(10.dp))

                FloatingMenuButtonItem(
                    navController = navController,
                    onClick = { navController.navigate(Screen.ProgressScreen.route) },
                    text = "Delete",
                    imageVector = Icons.Filled.Remove
                )

                Spacer(modifier = Modifier.size(10.dp))

                FloatingMenuButtonItem(
                    navController = navController,
                    onClick = { navController.navigate(Screen.ProgressScreen.route) },
                    text = "Export",
                    imageVector = Icons.Filled.ImportExport
                )

                Spacer(modifier = Modifier.size(10.dp))


                FloatingMenuButtonItem(
                    navController = navController,
                    onClick = { pickFileLauncher.launch(arrayOf("text/*")) },
                    text = "import",
                    imageVector = Icons.Filled.FileDownload
                )

                Spacer(modifier = Modifier.size(10.dp))

                FloatingMenuButtonItem(
                    navController = navController,
                    onClick = {navController.navigate("${Screen.LearnSessionScreen.route}?deckId=${viewDeckState.deckId}") },
                    text = "Practise",
                    imageVector = Icons.Filled.PlayArrow
                )
            }

            Spacer(modifier = Modifier.size(8.dp))
        }

        Spacer(modifier = Modifier.size(15.dp))

        FloatingActionButton(
            onClick = {
                viewModel.onEvent(ViewDeckEvent.ToggleActionMenu)
            },
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Icon(
                imageVector = Icons.Default.MenuOpen,
                contentDescription = "open options"
            )
        }
    }
}