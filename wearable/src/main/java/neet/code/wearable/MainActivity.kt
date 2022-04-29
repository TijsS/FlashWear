package neet.code.wearable


//import ClientDataViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.material.*
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import dagger.hilt.android.AndroidEntryPoint
import neet.code.wearable.feature_deck.DecksScreen
import neet.code.wearable.feature_deck.presentation.LearnStyle
import neet.code.wearable.feature_question.presentation.QuestionScreen
import neet.code.wearable.theme.FlashWearTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalWearMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlashWearTheme {
                val scalingLazyListState = rememberScalingLazyListState()
                Scaffold(
                    timeText = { TimeText() },
                    positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyListState) },
                    vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
                ) {
                    FlashWearNavHost()
                }
            }
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun FlashWearNavHost(modifier: Modifier = Modifier) {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Screen.DecksScreen.route,
        modifier = modifier
    ) {

        composable(route = Screen.DecksScreen.route) {
            DecksScreen(navController = navController)
        }

        composable(route = Screen.QuestionScreen.route + "?deckId={deckId}" + "?learnStyle={learnStyle}",
            arguments = listOf(
                navArgument(
                    name = "deckId"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(
                    name = "learnStyle"
                ) {
                    type = NavType.EnumType(LearnStyle::class.java)
                    defaultValue = LearnStyle.New
                }
            )
        ) {
            QuestionScreen(navController = navController)
        }
    }
}