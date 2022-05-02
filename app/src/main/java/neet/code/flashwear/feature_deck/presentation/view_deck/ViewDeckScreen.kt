package neet.code.flashwear.feature_deck.presentation.view_deck

import android.content.res.Configuration
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import neet.code.flashwear.R
import neet.code.flashwear.core.presentation.components.FlashWearDrawer
import neet.code.flashwear.core.presentation.components.FlashWearTopBar
import neet.code.flashwear.feature_deck.presentation.add_deck.AddDeckViewModel
import neet.code.flashwear.feature_deck.presentation.view_deck.components.FloatingMenuButton
import neet.code.flashwear.feature_deck.presentation.view_deck.components.ProgressTab
import neet.code.flashwear.feature_deck.presentation.view_deck.components.QuestionsTab
import neet.code.flashwear.ui.theme.FlashWearTheme


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewDeckScreen(
    navController: NavController,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    viewModel: ViewDeckViewModel = hiltViewModel()
){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewDeckState = viewModel.viewDeckState.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is ViewDeckViewModel.UiEvent.ShowSnackbar -> {
                    showSnackbar(
                        event.message, SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingMenuButton(
                viewModel,
                viewDeckState,
                navController,
                showSnackbar,
            )
        },
        topBar = {
            FlashWearTopBar(
                state = scaffoldState,
                scope = scope,
                title = viewDeckState.deckName,
                withFunction = false,
            )
        },
        drawerContent = {
            FlashWearDrawer(
                navController = navController,
                scope = scope
            )
        },
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
                    QuestionsTab(
                        viewDeckState = viewDeckState,
                        navController = navController,
                        viewModel = viewModel,
                        scope = scope,
                        scaffoldState = scaffoldState
                    )
                }
                DeckQuestionCategory.Progress -> {
                    if(viewModel.viewDeckState.value.avgScorePerX.isNotEmpty()){
                        ProgressTab()
                    }
                }
            }
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
                            DeckQuestionCategory.Questions -> stringResource(R.string.questions)
                            DeckQuestionCategory.Progress -> stringResource(R.string.progress)
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


