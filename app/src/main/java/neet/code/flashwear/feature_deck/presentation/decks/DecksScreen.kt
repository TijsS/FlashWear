package neet.code.flashwear.feature_deck.presentation.decks

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import neet.code.flashwear.R
import neet.code.flashwear.core.presentation.components.FlashWearDrawer
import neet.code.flashwear.core.presentation.components.FlashWearTopBar
import neet.code.flashwear.feature_deck.presentation.decks.components.DeckItem
import neet.code.flashwear.feature_deck.presentation.decks.components.OrderSection
import neet.code.flashwear.Screen
import neet.code.flashwear.feature_deck.presentation.decks.components.FloatingMenuButton
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckViewModel


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun DecksScreen(
    navController: NavController,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    viewModel: DeckViewModel = hiltViewModel()
){
    val deckState = viewModel.deckState.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is DeckViewModel.UiEvent.ShowSnackbar -> {
                    showSnackbar(
                        event.message, SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            if(deckState.decks?.isNotEmpty() == true) {
                FloatingMenuButton(viewModel, deckState, navController)
            }
        },
        topBar = {
            var functionVisible = false
            if(deckState.decks?.isNotEmpty() == true) {
                functionVisible = true
            }
                FlashWearTopBar(
                state = scaffoldState,
                scope = scope,
                title = stringResource(R.string.decks),
                actionFunction = {sortDecks(viewModel = viewModel)},
                withFunction = functionVisible,
                icon = Icons.Filled.MoreVert,
                onclickContentDescription = stringResource(R.string.sort_options)
            )
        },
        drawerContent = { FlashWearDrawer(
            navController = navController,
            scope = scope
        ) },
        scaffoldState = scaffoldState
    ) {
        if(deckState.decks?.isNotEmpty() == true) {
            Column() {
                AnimatedVisibility(
                    visible = deckState.isOrderSectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    OrderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        deckOrder = deckState.deckOrder,
                        onOrderChange = {
                            viewModel.onEvent(DecksEvent.Order(it))
                        }
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp)
                ) {
                    items(
                        count = deckState.decks.size,
                        key = null
                    )
                    { deckIndex ->
                        DeckItem(navController, deckState.decks.get(deckIndex))
                    }
                }
            }
        }else if(deckState.decks?.isEmpty() == true){
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(R.string.no_decks_message),
                    color = Color.White.copy(alpha = ContentAlpha.medium)
                )

                Spacer(modifier = Modifier.weight(0.2f))

                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.AddDeckScreen.route)
                    },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_deck))
                }

                Spacer(modifier = Modifier.weight(1.5f))

            }
        }
    }
}

fun sortDecks(
    viewModel: DeckViewModel
){
    viewModel.onEvent(DecksEvent.ToggleOrderSection)
}

