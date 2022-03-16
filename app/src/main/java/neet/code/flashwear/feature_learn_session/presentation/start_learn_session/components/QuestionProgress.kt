package neet.code.flashwear.feature_learn_session.presentation.start_learn_session.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.StartLearnSessionState
import neet.code.flashwear.ui.theme.DarkBlueGray

@Composable
fun QuestionProgress(startLearnSessionState: StartLearnSessionState) {

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .widthIn(max = 400.dp)
            .fillMaxWidth(),
    ) {
        repeat(startLearnSessionState.currentQuestionsBatch.size){
            //put all boxes on true when repeating questions
            if(it == startLearnSessionState.currentQuestionIndex){
                QuestionProgressBox(true)
            }
            else if(startLearnSessionState.currentQuestionIndex > startLearnSessionState.currentQuestionsBatch.size - 1){
                QuestionProgressBox(true)
            }
            else{
                QuestionProgressBox(false)
            }
        }
    }
}

@Composable
fun QuestionProgressBox(onPage: Boolean) {
    Box(
        modifier = Modifier
            .height(5.dp)
            .width(30.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (onPage) MaterialTheme.colors.secondary else DarkBlueGray)
    )
}