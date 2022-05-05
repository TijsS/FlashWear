package neet.code.flashwear.feature_settings.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import neet.code.flashwear.R
import neet.code.flashwear.core.presentation.components.FlashWearDrawer
import neet.code.flashwear.core.presentation.components.FlashWearTopBar
import neet.code.flashwear.feature_settings.domain.model.LearnStyle


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val settingsState = viewModel.settingsState
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        topBar = {
            FlashWearTopBar(
                state = scaffoldState,
                scope = scope,
                title = stringResource(R.string.settings),
                withFunction = false,
            )
        },
        drawerContent = {
            FlashWearDrawer(
                navController = navController,
                scope = scope
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(R.string.learn_style))

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(
                    onClick = {
                        viewModel.onEvent(SettingsEvent.ChangeLearnStyle(LearnStyle.New))
                    },
                    colors = buttonColors(backgroundColor = if (settingsState.value.settings.learnStyle == LearnStyle.New) MaterialTheme.colors.primary else Color.Black),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(text = stringResource(R.string.new_questions), color = Color.White)
                }


                Button(
                    onClick = {
                        viewModel.onEvent(SettingsEvent.ChangeLearnStyle(LearnStyle.Revise))
                    },
                    shape = RoundedCornerShape(4.dp),
                    colors = buttonColors(backgroundColor = if (settingsState.value.settings.learnStyle == LearnStyle.Revise) MaterialTheme.colors.primary else Color.Black)
                ) {
                    Text(text = stringResource(R.string.revise), color = Color.White)
                }


                Button(
                    onClick = {
                        viewModel.onEvent(SettingsEvent.ChangeLearnStyle(LearnStyle.Random))
                    },
                    colors = buttonColors(backgroundColor = if (settingsState.value.settings.learnStyle == LearnStyle.Random) MaterialTheme.colors.primary else Color.Black),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(text = stringResource(R.string.random), color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}