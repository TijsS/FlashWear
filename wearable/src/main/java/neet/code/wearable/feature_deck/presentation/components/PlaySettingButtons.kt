package neet.code.wearable.feature_deck.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import neet.code.wearable.R
import neet.code.wearable.core.presentation.CompactChipButton
import neet.code.wearable.feature_deck.presentation.DecksEvent
import neet.code.wearable.feature_deck.presentation.DecksState
import neet.code.wearable.feature_deck.presentation.DecksViewModel
import neet.code.wearable.feature_deck.presentation.LearnStyle

@Composable
fun PlaySettingButtons(deckState: DecksState, viewModel: DecksViewModel) {


        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
            ListHeader() {
                Text(text = "learnstyle", modifier = Modifier.padding(bottom = 5.dp, top = 13.dp))
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

            CompactChipButton(
                drawable = R.drawable.new_words,
                colors = ChipDefaults.chipColors(backgroundColor = if(deckState.learnStyle == LearnStyle.New) MaterialTheme.colors.primary else Color.Black),
                onClick = {viewModel.onEvent(DecksEvent.ChangeLearnStyle(LearnStyle.New))}
            )

            CompactChipButton(
                drawable = R.drawable.sync,
                colors = ChipDefaults.chipColors(backgroundColor = if(deckState.learnStyle == LearnStyle.Revise) MaterialTheme.colors.primary else Color.Black),
                onClick = {viewModel.onEvent(DecksEvent.ChangeLearnStyle(LearnStyle.Revise))}

            )

            CompactChipButton(
                drawable = R.drawable.shuffle,
                colors = ChipDefaults.chipColors(backgroundColor = if(deckState.learnStyle == LearnStyle.Random) MaterialTheme.colors.primary else Color.Black),
                onClick = {viewModel.onEvent(DecksEvent.ChangeLearnStyle(LearnStyle.Random))}
            )
        }
    }
}

