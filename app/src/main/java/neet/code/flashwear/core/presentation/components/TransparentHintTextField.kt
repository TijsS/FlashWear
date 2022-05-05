package neet.code.flashwear.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime.now

@Composable
fun TransparentHintTextField(
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    maxChars: Int = 200,
    maxLines: Int = 3,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {

    val scope = rememberCoroutineScope()
    val timeOfLastCharNotification = remember { mutableStateOf(now().minusSeconds(5)) }
    val timeOfLastLinesNotification = remember { mutableStateOf(now().minusSeconds(5)) }
    val previousValue = remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange =
        { value ->
                if(value.length > maxChars){
                    //only give notification if it has been 5 seconds since the last one
                    if (timeOfLastCharNotification.value.isBefore(now().minusSeconds(5))){
                        scope.launch {
                            showSnackbar("too long", SnackbarDuration.Short)
                        }
                        timeOfLastCharNotification.value = now()
                    }
                    onValueChange(previousValue.value)
                }
                else if(value.lines().size > maxLines){
                    if (timeOfLastLinesNotification.value.isBefore(now().minusSeconds(5))){
                        scope.launch {
                            showSnackbar("too many lines", SnackbarDuration.Short)
                        }
                        timeOfLastLinesNotification.value = now()
                    }
                    onValueChange(previousValue.value)
                }
                else{
                    previousValue.value = value
                    onValueChange(value)
                }
            },
        singleLine = singleLine,
        label = { Text(label) },
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        modifier = Modifier
            .fillMaxWidth()
    )
}
