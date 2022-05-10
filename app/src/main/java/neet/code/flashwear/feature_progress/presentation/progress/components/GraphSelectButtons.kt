package neet.code.flashwear.feature_progress.presentation.progress.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckEvent
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckViewModel
import neet.code.flashwear.feature_progress.presentation.progress.ProgressEvent
import neet.code.flashwear.feature_progress.presentation.progress.ProgressGraph
import neet.code.flashwear.feature_progress.presentation.progress.ProgressViewModel
import neet.code.flashwear.feature_progress.presentation.progress.TimeScaleGraph

@Composable
fun GraphSelectButtons(viewModel: ProgressViewModel) {
    Column() {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {
            for(option in ProgressGraph.values()) {
                TextButton(
                    onClick = { viewModel.onEvent(ProgressEvent.SelectProgressGraph(option)) },
                    modifier = Modifier.padding(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (viewModel.progressState.value.selectedProgressGraph == option) MaterialTheme.colors.primary else MaterialTheme.colors.surface)
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
                    onClick = { viewModel.onEvent(ProgressEvent.SelectedTimeScale(timeScale)) },
                    modifier = Modifier.padding(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (viewModel.progressState.value.selectedTimeScaleGraph == timeScale) MaterialTheme.colors.primary else MaterialTheme.colors.surface)
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