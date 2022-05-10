package neet.code.flashwear.feature_deck.presentation.view_deck.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import neet.code.flashwear.Screen
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckEvent
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckViewModel

@Composable
fun DeleteConfirmationBox(
    navController: NavController,
    viewModel: ViewDeckViewModel = hiltViewModel(),
) {
    Column(
        Modifier.fillMaxHeight()
    ) {

        Spacer(modifier = Modifier.weight(10f))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = RoundRectangle,
                color = MaterialTheme.colors.onSecondary
            ) {
                Column( horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Deleting is permanent, are you sure?",
                        modifier = Modifier.padding(10.dp, 15.dp),
                    )
                    Button(
                        onClick = {
                            viewModel.onEvent(ViewDeckEvent.DeleteDeck)
//                            navController.navigate(Screen.DecksScreen.route)
                            navController.popBackStack(Screen.DecksScreen.route, false)
                        },
                        Modifier.padding(5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colors.primary)
                    ) {
                        Text(text = "confirm")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(15f))
    }
}
