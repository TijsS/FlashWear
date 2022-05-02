package neet.code.flashwear.feature_deck.presentation.view_deck.components

import android.widget.Toast
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.coroutines.launch
import neet.code.flashwear.R
import neet.code.flashwear.Screen
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckEvent
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckState
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckViewModel
import neet.code.flashwear.feature_question.domain.model.Question
import java.time.LocalDate

@Composable
fun FloatingMenuButton(
    viewModel: ViewDeckViewModel,
    viewDeckState: ViewDeckState,
    navController: NavController,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    ) {
    val context = LocalContext.current
    val csvImportFailedMsgStringResource = stringResource(R.string.csv_import_fail)
    val csvExportFailedMsgStringResource = stringResource(R.string.csv_export_fail)

    val pickFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { csv ->
        if (csv != null) {
            val importedQuestions: MutableList<Question> = mutableListOf()

            context.contentResolver.openInputStream(csv)?.use { inputStream ->
                csvReader().open(inputStream){
                    readAllWithHeaderAsSequence().forEach { row ->

                        val question = Question(
                            questionTitle = row["QuestionTitle"],
                            questionContent = row["QuestionContent"],
                            answerTitle = row["AnswerTitle"],
                            answerContent = row["AnswerContent"],
                            answerSub = row["AnswerSub"],
                            deckId = viewDeckState.deckId
                        )
                        importedQuestions.add(question)
                    }
                }
            }

            viewModel.onEvent(ViewDeckEvent.ImportQuestions(importedQuestions))

        }else{
            showSnackbar(
                csvImportFailedMsgStringResource, SnackbarDuration.Short
            )
        }
    }

    val exportFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { path ->
        if (path != null) {

            context.contentResolver.openOutputStream(path)?.use { outputStream ->
                csvWriter().open(outputStream){
                    writeRow(
                        "QuestionTitle",
                        "QuestionContent",
                        "AnswerTitle",
                        "AnswerContent",
                        "AnswerSub"
                    )
                    for(question in viewDeckState.questions){
                        writeRow(question.toCsvRow())
                    }
                }
            }
            showSnackbar(
                "Exported", SnackbarDuration.Short
            )
        }else{
            showSnackbar(
                csvExportFailedMsgStringResource, SnackbarDuration.Short
            )
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
                    viewModel = viewModel,
                    onClick = { navController.navigate("${Screen.AddQuestionScreen.route}?deckId=${viewDeckState.deckId}") },
                    text = stringResource(R.string.add),
                    imageVector = Icons.Filled.Add
                )

                Spacer(modifier = Modifier.size(10.dp))

                FloatingMenuButtonItem(
                    viewModel = viewModel,
                    onClick = {
                        viewModel.onEvent(ViewDeckEvent.DeleteDeck)
                        navController.navigate(Screen.DecksScreen.route)
                              },
                    text = stringResource(R.string.delete),
                    imageVector = Icons.Filled.Remove
                )

                Spacer(modifier = Modifier.size(10.dp))

                FloatingMenuButtonItem(
                    viewModel = viewModel,
                    onClick = { exportFileLauncher.launch("${viewDeckState.deckName} - ${LocalDate.now()}") },
                    text = stringResource(R.string.export),
                    imageVector = Icons.Filled.ImportExport
                )

                Spacer(modifier = Modifier.size(10.dp))

                FloatingMenuButtonItem(
                    viewModel = viewModel,
                    onClick = { pickFileLauncher.launch(arrayOf("text/*")) },
                    text = stringResource(R.string.importText),
                    imageVector = Icons.Filled.FileDownload
                )

                Spacer(modifier = Modifier.size(10.dp))

                FloatingMenuButtonItem(
                    viewModel = viewModel,
                    onClick = {
                        viewModel.onEvent(ViewDeckEvent.SyncWithWearable)
                        ViewDeckViewModel.UiEvent.ShowSnackbar(
                            message = "synced"
                        )
                    },
                    text = stringResource(R.string.sync),
                    imageVector = Icons.Filled.Watch
                )

                Spacer(modifier = Modifier.size(10.dp))

                FloatingMenuButtonItem(
                    viewModel = viewModel,
                    onClick = {
                        navController.navigate(
                            Screen.LearnSessionScreen.route +
                                "?deckId=${viewDeckState.deckId}" +
                                "?deckName=${viewDeckState.deckName}") },
                    text = stringResource(R.string.learn),
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
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier.alpha(if (viewDeckState.isFloatingMenuVisible) 0.5f else 1f)
        ) {
            Icon(
                imageVector = Icons.Default.MenuOpen,
                contentDescription = stringResource(R.string.open_options)
            )
        }
    }
}