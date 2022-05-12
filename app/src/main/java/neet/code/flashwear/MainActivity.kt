package neet.code.flashwear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import neet.code.flashwear.core.presentation.components.DefaultSnackbar
import neet.code.flashwear.core.presentation.components.SnackbarDemoAppState
import neet.code.flashwear.core.presentation.components.rememberSnackbarDemoAppState
import neet.code.flashwear.feature_deck.presentation.add_deck.AddDeckScreen
import neet.code.flashwear.feature_deck.presentation.decks.DecksScreen
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckScreen
import neet.code.flashwear.feature_question.presentation.add_question.AddQuestionScreen
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.StartLearnSessionScreen
import neet.code.flashwear.feature_progress.presentation.progress.ProgressScreen
import neet.code.flashwear.feature_question.presentation.view_question.ViewQuestionScreen
import neet.code.flashwear.feature_settings.presentation.SettingsScreen

import neet.code.flashwear.ui.theme.FlashWearTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashWearTheme {
                FlashWearApp()
            }
        }
    }
}

@Composable
fun FlashWearApp(){

    val navController = rememberNavController()

    FlashWearNavHost(navController)
}


@Composable
fun FlashWearNavHost(navController: NavHostController, modifier: Modifier = Modifier){
    val context = LocalContext.current

    val appState: SnackbarDemoAppState = rememberSnackbarDemoAppState(context)

    Box() {
        NavHost(
            navController = navController,
            startDestination = Screen.DecksScreen.route,
            modifier = modifier
        ) {
            composable(route = Screen.DecksScreen.route) {
                DecksScreen(
                    navController = navController,
                    showSnackbar = { message, duration ->
                        appState.showSnackbar(message = message, R.string.dismiss, duration = duration)
                    }
                )
            }

            composable(route = Screen.AddDeckScreen.route) {
                AddDeckScreen(
                    navController = navController,
                    showSnackbar = { message, duration ->
                        appState.showSnackbar(message = message, R.string.dismiss, duration = duration)
                    }
                )
            }

            composable(route = Screen.ProgressScreen.route) {
                ProgressScreen(
                    navController = navController,
                    showSnackbar = { message, duration ->
                        appState.showSnackbar(message = message, R.string.dismiss, duration = duration)
                    }
                )
            }

            composable(route = Screen.SettingsScreen.route) {
                SettingsScreen(
                    navController = navController,
                    showSnackbar = { message, duration ->
                        appState.showSnackbar(message = message, R.string.dismiss, duration = duration)
                    }
                )
            }

            composable(route = Screen.ViewDeckScreen.route + "?deckId={deckId}?deckName={deckName}",
                arguments = listOf(
                    navArgument(
                        name = "deckId"
                    ) {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                    navArgument(
                        name = "deckName"
                    ) {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ) {
                ViewDeckScreen(
                    navController = navController,
                    showSnackbar = { message, duration ->
                        appState.showSnackbar(message = message, R.string.dismiss, duration = duration)
                    })
            }

            composable(route = Screen.AddQuestionScreen.route + "?deckId={deckId}",
                arguments = listOf(
                    navArgument(
                        name = "deckId"
                    ) {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) {
                AddQuestionScreen(
                    navController = navController,
                    showSnackbar = { message, duration ->
                        appState.showSnackbar(message = message, R.string.dismiss, duration = duration)
                    })
            }

            composable(route = Screen.LearnSessionScreen.route + "?deckId={deckId}" + "?deckName={deckName}",
                arguments = listOf(
                    navArgument(
                        name = "deckId"
                    ) {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                    navArgument(
                        name = "deckName"
                    ) {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ) {
                StartLearnSessionScreen(
                    navController = navController,
                    showSnackbar = { message, duration ->
                        appState.showSnackbar(message = message, R.string.dismiss, duration = duration)
                    })
            }

            composable(route = Screen.ViewQuestionScreen.route + "?questionId={questionId}",
                arguments = listOf(
                    navArgument(
                        name = "questionId"
                    ) {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) {
                ViewQuestionScreen(
                    navController = navController,
                    showSnackbar = { message, duration ->
                        appState.showSnackbar(message = message, R.string.dismiss, duration = duration)
                    }
                )
            }
        }
        Column() {
            Spacer(modifier = Modifier.weight(2f))

            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                , Arrangement.Center, ) {
                Spacer(modifier = Modifier.weight(0.1f))
                DefaultSnackbar(
                    snackbarHostState = appState.scaffoldState.snackbarHostState,
                    onDismiss = {
                        appState.scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    },
                    modifier = Modifier
                        .weight(2f)
                )
                Spacer(modifier = Modifier.weight(0.6f))
            }
        }
    }
}