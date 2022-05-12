package neet.code.flashwear.feature_deck.presentation.add_deck

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import neet.code.flashwear.R
import neet.code.flashwear.core.presentation.components.FlashWearDrawer
import neet.code.flashwear.core.presentation.components.FlashWearTopBar
import neet.code.flashwear.core.presentation.components.TransparentHintTextField


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddDeckScreen(
    navController: NavController,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    viewModel: AddDeckViewModel = hiltViewModel()
){
    val scope = rememberCoroutineScope()
    val deckState = viewModel.deck.value
    val scaffoldState = rememberScaffoldState()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddDeckViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(event.message)
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
                Icon(imageVector = Icons.Default.Save, contentDescription = stringResource(R.string.save_deck))
            }
        },
        topBar = {
            FlashWearTopBar(
                state = scaffoldState,
                scope = scope,
                title = stringResource(R.string.add_deck),
                withFunction = false,
            )
        },
        drawerContent = { FlashWearDrawer(
            navController = navController,
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
                label = stringResource(R.string.deck_title),
                onValueChange = {
                    viewModel.onEvent(AddDeckEvent.EnteredName(it))
                },
                showSnackbar = showSnackbar,
                singleLine = true,
                maxChars = 40,
                maxLines = 1,
                textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary, fontSize = MaterialTheme.typography.body1.fontSize))
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}