package neet.code.flashwear.feature_deck.presentation.view_deck.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun DeleteConfirmationBox() {
    Column(
        Modifier.fillMaxHeight()
    ) {

        Spacer(modifier = Modifier.weight(10f))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = RoundRectangle
            ) {
                Column( horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Deleting is permanent, are you sure?",
                        modifier = Modifier.padding(10.dp, 15.dp),
                    )
                    Button(
                        onClick = { /*TODO*/ },
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
