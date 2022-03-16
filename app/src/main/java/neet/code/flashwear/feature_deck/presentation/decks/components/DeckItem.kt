package neet.code.flashwear.feature_deck.presentation.decks.components

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.Screen
import neet.code.flashwear.feature_deck.presentation.decks.DeckViewModel
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckViewModel

@Composable

fun DeckItem(navController: NavController, deck: Deck, viewModel: DeckViewModel = hiltViewModel()
) {
    Row(modifier = Modifier.padding(all = 8.dp)) {

        Column {
            Text(
                text = deck.name,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier.padding(bottom = 1.dp, start = 4.dp)
            )

            Spacer(modifier = Modifier.height(1.dp))
            TabRowDefaults.Divider(color = Color.Red, thickness = 1.dp, )
            Spacer(Modifier.height(10.dp))

            //show % of completed questions of deck
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(15.dp))

                viewModel.deckState.value.averageScoreOfDecks[deck.id]?.let {
                    LinearProgressIndicator(
                        progress = it,
                        Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))
            }

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = {
                    navController.navigate(
                    Screen.ViewDeckScreen.route +
                            "?deckId=${deck.id}"
                )},
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "View")
            }

            Spacer(Modifier.height(3.dp))

            Button(
                onClick = {
                    navController.navigate(
                        Screen.LearnSessionScreen.route +
                                "?deckId=${deck.id}"
                    )},
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Practise")
            }
        }
    }
}