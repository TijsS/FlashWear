package neet.code.flashwear.feature_deck.presentation.view_deck.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import neet.code.flashwear.R
import neet.code.flashwear.core.presentation.components.DefaultSnackbar
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckEvent
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckState
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionsTab(
    viewDeckState: ViewDeckState,
    navController: NavController,
    viewModel: ViewDeckViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
){

    val questionDeletedMessage = stringResource(R.string.question_deleted_message)
    val questionDeletedActionMessage = stringResource(R.string.question_deleted_action_message)


    if (viewDeckState.questions.isNotEmpty()) {
        Column(
            Modifier.fillMaxWidth()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 450.dp),
            ) {
                items(
                    count = viewDeckState.questions.size,
                    key = null
                ) { questionIndex ->

                    val question = viewDeckState.questions[questionIndex]

                    val infiniteTransition = rememberInfiniteTransition()
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = if (viewDeckState.questionIsHeldForDelete) 0.95f else 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    Row {
                        //Scale the box for breathing effect, for deletion of question
                        Box(
                            modifier = Modifier
                                .scale(scale)
                                .weight(1f)
                        ) {
                            QuestionAnswerItem(
                                question = question,
                                navController = navController
                            )
                        }

                        //Make Delete button visible when question is held
                        AnimatedVisibility(
                            visible = viewDeckState.questionIsHeldForDelete,
                            enter = fadeIn(
                                initialAlpha = 0f,
                                animationSpec = tween(
                                    durationMillis = 450,
                                    easing = LinearEasing
                                )
                            ) +
                                    slideInHorizontally(
                                        initialOffsetX = { 150 },
                                        animationSpec = tween(
                                            durationMillis = 300,
                                            easing = LinearEasing
                                        )
                                    ),
                            exit = fadeOut() +
                                    slideOutHorizontally(
                                        targetOffsetX = { 150 },
                                        animationSpec = tween(
                                            durationMillis = 300,
                                            easing = LinearEasing
                                        )
                                    )
                        ) {
                            IconButton(
                                onClick = {
                                    viewModel.onEvent(ViewDeckEvent.DeleteQuestion(question))
                                    scope.launch {
                                        val result =
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message = questionDeletedMessage,
                                                actionLabel = questionDeletedActionMessage,
                                            )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            viewModel.onEvent(ViewDeckEvent.RestoreQuestion)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = stringResource(R.string.delete)
                                )
                            }
                        }
                    }
                }
            }
        }

    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.no_questions_message),
                color = Color.White.copy(alpha = ContentAlpha.medium)
            )
            Spacer(modifier = Modifier.weight(1.6f))

        }
    }
}