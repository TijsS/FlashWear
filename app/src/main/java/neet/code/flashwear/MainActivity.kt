package neet.code.flashwear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import neet.code.flashwear.feature_deck.presentation.add_deck.AddDeckScreen
import neet.code.flashwear.feature_deck.presentation.decks.DecksScreen
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckScreen
import neet.code.flashwear.feature_question.presentation.add_question.AddQuestionScreen
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.StartLearnSessionScreen
import neet.code.flashwear.feature_settings.presentation.SettingsScreen

import neet.code.flashwear.ui.progress.ProgressBody
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
    NavHost(
        navController = navController,
        startDestination = Screen.DecksScreen.route,
        modifier = modifier
    ) {
        composable(route = Screen.DecksScreen.route) {
            DecksScreen(navController = navController)
        }

        composable(route = Screen.AddDeckScreen.route) {
            AddDeckScreen(navController = navController)
        }

        composable(route = Screen.ProgressScreen.route) {
            ProgressBody(navController = navController)
        }

        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }

        composable(route = Screen.ViewDeckScreen.route+ "?deckId={deckId}",
            arguments = listOf(
                navArgument(
                    name = "deckId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            ViewDeckScreen(navController = navController)
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
        ){
            AddQuestionScreen(navController = navController)
        }

        composable(route = Screen.LearnSessionScreen.route + "?deckId={deckId}",
            arguments = listOf(
                navArgument(
                    name = "deckId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ){
            StartLearnSessionScreen(navController = navController)
        }
    }
}