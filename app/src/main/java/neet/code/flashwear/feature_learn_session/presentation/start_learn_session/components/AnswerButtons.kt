package neet.code.flashwear.feature_learn_session.presentation.start_learn_session.components

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import neet.code.flashwear.R
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.StartLearnSessionEvent
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.StartLearnSessionState
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.StartLearnSessionViewModel

@Composable
fun UserAnswer(startLearnSessionState: StartLearnSessionState, viewModel: StartLearnSessionViewModel) {

    Box(
        Modifier.padding(20.dp)
    ) {

        //Show at start and hide when answer is shown
        AnimatedVisibility(
            visible = !startLearnSessionState.showAnswer,
            enter = fadeIn() + slideInVertically(
                initialOffsetY = { -150 },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearEasing
                )
            ),
            exit = fadeOut() + slideOutVertically(
                targetOffsetY = { 150 },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearEasing
                )
            )
        ) {
            Button(
                onClick = { viewModel.onEvent(StartLearnSessionEvent.ShowAnswer) },
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = stringResource(R.string.show))
            }
        }

        //Hide at start and show when answer is shown
        AnimatedVisibility(
            visible = startLearnSessionState.showAnswer,
            enter = fadeIn() + slideInVertically(
                initialOffsetY = { -150 },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearEasing
                )
            ),
            exit = fadeOut() + slideOutVertically(
                targetOffsetY = { 150 },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearEasing
                )
            ),
            modifier = Modifier
                .fillMaxWidth(),

            ) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {

                Button(
                    onClick = { viewModel.onEvent(StartLearnSessionEvent.NextQuestion("-"))},
                    modifier = Modifier
                        .widthIn(max = 150.dp)
                        .weight(3f)
                        .fillMaxWidth()
                        .padding(2.dp)
                        .height(50.dp),
                ) {
                    Text(text = stringResource(R.string.incorrect))
                }

                Button(
                    onClick = { viewModel.onEvent(StartLearnSessionEvent.NextQuestion("="))  },
                    modifier = Modifier
                        .widthIn(max = 150.dp)
                        .weight(3f)
                        .fillMaxWidth()
                        .padding(2.dp)
                        .height(50.dp),
                ) {
                    Text(text = stringResource(R.string.almost))
                }

                Button(
                    onClick = { viewModel.onEvent(StartLearnSessionEvent.NextQuestion("+"))  },
                    modifier = Modifier
                        .widthIn(max = 150.dp)
                        .weight(3f)
                        .fillMaxWidth()
                        .padding(2.dp)
                        .height(50.dp),
                ) {
                    Text(text = stringResource(R.string.correct))
                }
            }
        }
    }
}
