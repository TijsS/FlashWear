package neet.code.flashwear.feature_deck.presentation.add_deck.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun TransparentHintTextField(
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        singleLine = singleLine,
        label = { Text(label) },
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        modifier = Modifier
            .fillMaxWidth()
    )
}
