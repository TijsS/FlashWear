package neet.code.flashwear.feature_deck.presentation.add_deck.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import neet.code.flashwear.feature_deck.presentation.add_deck.AddDeckEvent

@Composable
fun QuestionAnswerField(
    ){

    val questionState = remember { mutableStateOf(TextFieldValue()) }
    val answerState = remember { mutableStateOf(TextFieldValue()) }

    Box(
    ) {
        TextField(
            value = questionState.value,
            onValueChange = { questionState.value = it },
            singleLine = false,
            label = { Text("Question") },
            textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary)),
            modifier = Modifier
                .fillMaxWidth()

        )
    }

    Spacer(modifier = Modifier.width(5.dp))

    Box(
    ) {
        TextField(
            value = answerState.value,
            onValueChange = { answerState.value = it },
            singleLine = false,
            label = { Text("Answer") },
            textStyle = MaterialTheme.typography.h5.plus(TextStyle(color = MaterialTheme.colors.onPrimary)),
            modifier = Modifier
                .fillMaxWidth()

        )
    }
}