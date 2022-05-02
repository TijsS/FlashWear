package neet.code.flashwear.feature_learn_session.presentation.start_learn_session.components

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp



@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun Card(
    text: String,
    maxLines: Int,
    paddingValues: PaddingValues,
    size: MutableState<TextUnit>
) {

    Column(verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = size.value,
            overflow = TextOverflow.Clip,
            maxLines = maxLines,
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = 8.dp, end = 8.dp)
            ,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.didOverflowHeight) {
                    size.value=size.value* 0.9f
                }
            }
        )
    }
}