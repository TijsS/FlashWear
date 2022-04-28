package neet.code.wearable.feature_deck

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.*
import neet.code.wearable.Screen
import neet.code.wearable.feature_deck.presentation.DecksViewModel
import neet.code.wearable.feature_deck.presentation.components.PlaySettingButtons


@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun DecksScreen(
    navController: NavController,
    viewModel: DecksViewModel = hiltViewModel()
) {

    val viewDeckState = viewModel.deckState.value
    val scalingLazyListState = rememberScalingLazyListState()


    ScalingLazyColumn(
        state = scalingLazyListState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item{
            PlaySettingButtons(viewDeckState, viewModel)
        }


        items(viewModel.deckState.value.decks) { deck ->
            Chip(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    navController.navigate( Screen.QuestionScreen.route +
                            "?deckId=${deck.id}" +
                            "?learnStyle=${viewDeckState.learnStyle}") },
                label = {
                    Text(
                        text = deck.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "play icon",
                        modifier = Modifier
                            .size(24.dp)
                            .wrapContentSize(align = Alignment.Center),
                    )
                }
            )
        }
    }
}

