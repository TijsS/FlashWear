package neet.code.flashwear.feature_deck.presentation.add_deck

import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.flow.collectLatest
import neet.code.flashwear.core.presentation.components.FlashWearDrawer
import neet.code.flashwear.core.presentation.components.FlashWearTopBar
import neet.code.flashwear.feature_deck.presentation.add_deck.components.TransparentHintTextField
import neet.code.flashwear.feature_question.domain.model.Question
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddDeckScreen(    navController: NavController,
                     viewModel: AddDeckViewModel = hiltViewModel()
){
    val scope = rememberCoroutineScope()
    val deckState = viewModel.deck.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddDeckViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddDeckViewModel.UiEvent.SaveDeck -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddDeckEvent.AddDeck)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save deck")
            }
        },
        topBar = {
            FlashWearTopBar(
                state = scaffoldState,
                scope = scope,
                title = "Add deck",
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
                text = deckState.text,
                label = "Deck title",
                onValueChange = {
                    viewModel.onEvent(AddDeckEvent.EnteredName(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary))
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}