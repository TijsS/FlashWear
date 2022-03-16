package neet.code.flashwear.feature_settings.presentation

import android.content.ContentValues.TAG
import android.util.Log
import neet.code.flashwear.feature_question.presentation.add_question.AddQuestionEvent
import neet.code.flashwear.feature_question.presentation.add_question.AddQuestionViewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import neet.code.flashwear.core.presentation.components.FlashWearDrawer
import neet.code.flashwear.core.presentation.components.FlashWearTopBar
import neet.code.flashwear.feature_deck.presentation.add_deck.components.TransparentHintTextField
import neet.code.flashwear.feature_deck.presentation.decks.DecksEvent
import neet.code.flashwear.feature_settings.domain.model.LearnStyle


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
){
    val scope = rememberCoroutineScope()
    val settingsState = viewModel.settingsState
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {
            FlashWearTopBar(
                state = scaffoldState,
                scope = scope,
                title = "Change Settings",
                withFunction = false,
            )
        },
        drawerContent = { FlashWearDrawer(
            navController = navController,
            scaffoldState = scaffoldState,
            scope = scope
        ) },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Set the practise style")

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {

                Button(
                    onClick = {
                        viewModel.onEvent(SettingsEvent.ChangeLearnStyle(LearnStyle.NewWords))
                    },
                    colors = buttonColors(backgroundColor = if(settingsState.value.settings.learnStyle == LearnStyle.NewWords) MaterialTheme.colors.primary else Color.Black),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(text = "New Words", color = Color.White)
                }


                Button(
                    onClick = {
                        viewModel.onEvent(SettingsEvent.ChangeLearnStyle(LearnStyle.Revise))
                    },
                    shape = RoundedCornerShape(4.dp),
                    colors = buttonColors(backgroundColor = if(settingsState.value.settings.learnStyle == LearnStyle.Revise) MaterialTheme.colors.primary else Color.Black)
                    ) {
                    Text(text = "Revise", color = Color.White)
                }


                Button(
                    onClick = {
                        viewModel.onEvent(SettingsEvent.ChangeLearnStyle(LearnStyle.Random))
                    },
                    colors = buttonColors(backgroundColor = if(settingsState.value.settings.learnStyle == LearnStyle.Random) MaterialTheme.colors.primary else Color.Black),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(text = "Random", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}