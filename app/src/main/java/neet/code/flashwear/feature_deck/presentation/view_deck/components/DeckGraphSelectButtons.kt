package neet.code.flashwear.feature_deck.presentation.view_deck.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import neet.code.flashwear.feature_deck.presentation.view_deck.ProgressDeckGraph
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckEvent
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckViewModel
import neet.code.flashwear.feature_progress.presentation.progress.ProgressGraph
import neet.code.flashwear.feature_progress.presentation.progress.TimeScaleGraph
import neet.code.flashwear.ui.theme.DarkBlueGray

@Composable
fun DeckGraphSelectButtons(viewModel: ViewDeckViewModel) {
    Column() {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {
            for(option in ProgressDeckGraph.values()) {
                TextButton(
                    onClick = { viewModel.onEvent(ViewDeckEvent.SelectProgressGraph(option)) },
                    modifier = Modifier.padding(4.dp),
                    colors = buttonColors(containerColor = if (viewModel.viewDeckState.value.selectedProgressGraph == option) MaterialTheme.colors.primary else if (isSystemInDarkTheme()) MaterialTheme.colors.surface else MaterialTheme.colors.onPrimary)
                ) {
                    Text(
                        text = option.name,
                        color = Color.White
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth(),
        ) {
            for(timeScale in TimeScaleGraph.values()) {
                TextButton(
                    onClick = { viewModel.onEvent(ViewDeckEvent.SelectedTimeScale(timeScale)) },
                    modifier = Modifier.padding(4.dp),
                    colors = buttonColors(containerColor = if (viewModel.viewDeckState.value.selectedTimeScaleGraph == timeScale) MaterialTheme.colors.primary else if (isSystemInDarkTheme()) MaterialTheme.colors.surface else MaterialTheme.colors.onPrimary,
                        MaterialTheme.colors.surface)
                ) {
                    Text(
                        text = timeScale.name,
                        color = Color.White
                    )
                }
            }
        }
    }
}