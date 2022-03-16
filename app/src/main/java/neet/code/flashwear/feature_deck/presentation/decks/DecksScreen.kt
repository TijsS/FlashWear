package neet.code.flashwear.feature_deck.presentation.decks

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import neet.code.flashwear.core.presentation.components.FlashWearDrawer
import neet.code.flashwear.core.presentation.components.FlashWearTopBar
import neet.code.flashwear.feature_deck.presentation.decks.components.DeckItem
import neet.code.flashwear.feature_deck.presentation.decks.components.OrderSection
import neet.code.flashwear.Screen



@OptIn(ExperimentalFoundationApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun DecksScreen(navController: NavController,
              viewModel: DeckViewModel = hiltViewModel()
){
    val deckState = viewModel.deckState.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddDeckScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Deck")
            }
        },
        topBar = {
            FlashWearTopBar(
                state = scaffoldState,
                scope = scope,
                title = "Question Decks",
                actionFunction = {sortDecks(viewModel = viewModel)},
                withFunction = true,
                icon = Icons.Filled.MoreVert
            )
        },
        drawerContent = { FlashWearDrawer(
            navController = navController,
            scaffoldState = scaffoldState,
            scope = scope
        ) },
        scaffoldState = scaffoldState
    ) {
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
                cells = GridCells.Adaptive(minSize = 150.dp)
            ) {
                items(deckState.decks) { deck ->
                    DeckItem(navController, deck)
                }
            }

        }
    }
}

fun sortDecks(
    viewModel: DeckViewModel
){
    viewModel.onEvent(DecksEvent.ToggleOrderSection)
}