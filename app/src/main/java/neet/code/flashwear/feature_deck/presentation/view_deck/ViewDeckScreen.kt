package neet.code.flashwear.feature_deck.presentation.view_deck

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import neet.code.flashwear.core.presentation.components.FlashWearDrawer
import neet.code.flashwear.core.presentation.components.FlashWearTopBar
import neet.code.flashwear.feature_deck.presentation.view_deck.components.FloatingMenuButton
import neet.code.flashwear.feature_deck.presentation.view_deck.components.QuestionAnswerItem
import neet.code.flashwear.ui.theme.FlashWearTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewDeckScreen(navController: NavController,
                   viewModel: ViewDeckViewModel = hiltViewModel()
){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewDeckState = viewModel.viewDeckState.value

    Scaffold(
        floatingActionButton = {
            FloatingMenuButton(viewModel, viewDeckState, navController)
        },
        topBar = {
            FlashWearTopBar(
                state = scaffoldState,
                scope = scope,
                title = "Question Decks",
                withFunction = false,
            )
        },
        drawerContent = {
            FlashWearDrawer(
                navController = navController,
                scaffoldState = scaffoldState,
                scope = scope
        ) },
        scaffoldState = scaffoldState
    ) {

        Column() {
            if (viewDeckState.categories.isNotEmpty()) {
                QuestionCategoryTabs(
                    categories = viewDeckState.categories,
                    selectedCategory = viewDeckState.selectedTab,
                    onCategorySelected = viewModel::onCategorySelected
                )
            }

            when (viewDeckState.selectedTab) {
                DeckQuestionCategory.Questions -> {
                    LazyVerticalGrid(
                        cells = GridCells
                            .Adaptive(
                                minSize = 450.dp
                            ),
                    ) {
                        items(viewDeckState.questions) { question ->

                            val infiniteTransition = rememberInfiniteTransition()
                            val scale by infiniteTransition.animateFloat(
                                initialValue = 1f,
                                targetValue = if(viewDeckState.questionIsHeldForDelete) 0.95f else 1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1200),
                                    repeatMode = RepeatMode.Reverse
                                )
                            )

                            Row() {
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
                                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                                    message = "Question deleted",
                                                    actionLabel = "Undo"
                                                )
                                                if(result == SnackbarResult.ActionPerformed) {
                                                    viewModel.onEvent(ViewDeckEvent.RestoreQuestion)
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                                    }
                                }
                            }
                        }
                    }
                }
                DeckQuestionCategory.Progress -> {
                    //TODO give overview + tips on words
                }
            }
        }
    }
}




@Composable
fun CircularProgressAnimated(score: Double) {
    CircularProgressIndicator(
        progress = score.toFloat(),
        modifier = Modifier.padding(5.dp)
    )
}

@Composable
fun DeckQuestion(question: String) {
    Text(question,
    modifier = Modifier
        .requiredHeightIn(max = 60.dp),
    overflow = TextOverflow.Clip
    )
}

@Composable
fun DeckAnswer(answer: String) {
    Text(answer,
        modifier = Modifier
            .requiredHeightIn(max = 60.dp),
        overflow = TextOverflow.Ellipsis
    )
}

@Preview("Simple question Answer")
@Preview("Simple post card (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SimpleQuestionAnswerPreview() {
    FlashWearTheme {
        Surface {
            ViewDeckScreen(navController = rememberNavController())
        }
    }
}

@Composable
private fun QuestionCategoryTabs(
    categories: List<DeckQuestionCategory>,
    selectedCategory: DeckQuestionCategory,
    onCategorySelected: (DeckQuestionCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = categories.indexOfFirst { it == selectedCategory }
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        HomeCategoryTabIndicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedIndex])
        )
    }

    TabRow(
        selectedTabIndex = selectedIndex,
        indicator = indicator,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.onSecondary
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onCategorySelected(category) },
                text = {
                    Text(
                        text = when (category) {
                            DeckQuestionCategory.Questions -> "Questions"
                            DeckQuestionCategory.Progress -> "Progress"
                        },
                        style = MaterialTheme.typography.body2
                    )
                }
            )
        }
    }
}

@Composable
fun HomeCategoryTabIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface
) {
    Spacer(
        modifier
            .padding(horizontal = 24.dp)
            .height(4.dp)
            .background(color, RoundedCornerShape(topStartPercent = 100, topEndPercent = 100))
    )
}



