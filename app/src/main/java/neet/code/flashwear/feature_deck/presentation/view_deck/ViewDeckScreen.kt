package neet.code.flashwear.feature_deck.presentation.view_deck

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import neet.code.flashwear.R
import neet.code.flashwear.core.presentation.components.FlashWearDrawer
import neet.code.flashwear.core.presentation.components.FlashWearTopBar
import neet.code.flashwear.feature_deck.presentation.view_deck.components.DeleteConfirmationBox
import neet.code.flashwear.feature_deck.presentation.view_deck.components.FloatingMenuButton
import neet.code.flashwear.feature_deck.presentation.view_deck.components.ProgressTab
import neet.code.flashwear.feature_deck.presentation.view_deck.components.QuestionsTab


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

        Column {
            Box {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .clickable{
                    viewModel.onEvent(ViewDeckEvent.CloseDeleteBox)
                }) {
                    if (viewDeckState.categories.isNotEmpty()) {
                        DeckCategoryTabs(
                            categories = viewDeckState.categories,
                            selectedCategory = viewDeckState.selectedTab,
                            onCategorySelected = viewModel::onCategorySelected
                        )
                    }

                    when (viewDeckState.selectedTab) {
                        DeckCategory.Questions -> {
                            QuestionsTab(
                                viewDeckState = viewDeckState,
                                navController = navController,
                                viewModel = viewModel,
                                scope = scope,
                                scaffoldState = scaffoldState
                            )
                        }

                        DeckCategory.Progress -> {
                            if (viewModel.viewDeckState.value.avgScoresLine.isNotEmpty()) {
                                ProgressTab()
                            }
                        }
                    }
                }

                if (viewDeckState.showDeleteDeckBox) {
                    DeleteConfirmationBox(navController)
                }
            }
        }
    }
}

@Composable
private fun DeckCategoryTabs(
    categories: List<DeckCategory>,
    selectedCategory: DeckCategory,
    onCategorySelected: (DeckCategory) -> Unit,
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
                            DeckCategory.Questions -> stringResource(R.string.questions)
                            DeckCategory.Progress -> stringResource(R.string.progress)
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



