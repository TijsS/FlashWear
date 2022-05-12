package neet.code.flashwear.feature_deck.presentation.decks.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import neet.code.flashwear.feature_deck.presentation.decks.DeckViewModel
import neet.code.flashwear.feature_deck.presentation.decks.DecksEvent


@Composable
fun FloatingMenuButtonItem(viewModel: DeckViewModel, onClick: () -> Unit, text: String, imageVector: ImageVector) {
    Surface(
        color = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(9.dp),
    ) {
        IconButton(
            onClick = {
                viewModel.onEvent(DecksEvent.ToggleActionMenu)
                onClick()
            },
            modifier = Modifier
                .width(100.dp)
        ) {
            Row() {
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = imageVector,
                    contentDescription = text,
                    Modifier
                        .size(20.dp),
                )
                Spacer(Modifier.size(5.dp))
                Text(text = text)
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}