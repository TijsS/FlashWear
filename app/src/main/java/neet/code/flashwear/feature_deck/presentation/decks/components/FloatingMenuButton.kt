package neet.code.flashwear.feature_deck.presentation.decks.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import neet.code.flashwear.R
import neet.code.flashwear.Screen
import neet.code.flashwear.feature_deck.presentation.add_deck.AddDeckViewModel
import neet.code.flashwear.feature_deck.presentation.decks.DeckViewModel
import neet.code.flashwear.feature_deck.presentation.decks.DecksEvent
import neet.code.flashwear.feature_deck.presentation.decks.DecksState

@Composable
fun FloatingMenuButton(viewModel: DeckViewModel, deckState: DecksState, navController: NavController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = deckState.isFloatingMenuVisible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column {

                FloatingMenuButtonItem(
                    viewModel = viewModel,
                    onClick = { navController.navigate(Screen.AddDeckScreen.route) },
                    text = stringResource(R.string.add),
                    imageVector = Icons.Filled.Add
                )

                Spacer(modifier = Modifier.size(10.dp))

                FloatingMenuButtonItem(
                    viewModel = viewModel,
                    onClick = {
                        viewModel.onEvent(DecksEvent.SyncWithWearable)
                    },
                    text = stringResource(R.string.sync),
                    imageVector = Icons.Filled.Watch
                )
            }

            Spacer(modifier = Modifier.size(8.dp))
        }

        Spacer(modifier = Modifier.size(15.dp))

        FloatingActionButton(
            onClick = {
                viewModel.onEvent(DecksEvent.ToggleActionMenu)
            },
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier.alpha(if (deckState.isFloatingMenuVisible) 0.5f else 1f)

        ) {
            Icon(
                imageVector = Icons.Default.MenuOpen,
                contentDescription = stringResource(R.string.decks_menu),
            )
        }
    }
}