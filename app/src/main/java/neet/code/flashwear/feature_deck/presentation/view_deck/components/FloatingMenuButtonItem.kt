package neet.code.flashwear.feature_deck.presentation.view_deck.components

import android.content.ContentValues
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import neet.code.flashwear.Screen
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckState
import neet.code.flashwear.ui.theme.FlashWearTheme
import neet.code.flashwear.ui.theme.TextInput


@Composable
fun FloatingMenuButtonItem(navController: NavController, onClick: () -> Unit, text: String, imageVector: ImageVector, viewDeckState: ViewDeckState? = null) {
    Surface(
        color = TextInput,
        shape = RoundedCornerShape(9.dp),
    ) {
        IconButton(
            onClick = {
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

@Preview("floating button menu")
@Preview("floating button menu", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FloatingButtonPreview() {
    val navController: NavController =  rememberNavController()
    FlashWearTheme {
        FloatingMenuButtonItem(
            navController = rememberNavController(),
            onClick = { navController.navigate(Screen.ProgressScreen.route) },
            text = "Prdfsdfg",
            imageVector = Icons.Filled.PlayArrow
        )
    }
}