package neet.code.flashwear.feature_learn_session.presentation.start_learn_session

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import neet.code.flashwear.R
import neet.code.flashwear.Screen
import neet.code.flashwear.core.presentation.components.FlashWearDrawer
import neet.code.flashwear.core.presentation.components.FlashWearTopBar
import neet.code.flashwear.feature_deck.presentation.decks.DeckViewModel
import neet.code.flashwear.feature_learn_session.domain.use_case.StartLearnSession
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.components.AnswerCard
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.components.QuestionCard
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.components.QuestionProgress
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.components.UserAnswer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartLearnSessionScreen(
    navController: NavController,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    viewModel: StartLearnSessionViewModel = hiltViewModel()
){
    val scope = rememberCoroutineScope()
    val startLearnSessionState = viewModel.learnSession.value
    val scaffoldState = rememberScaffoldState()
    val sizeTitle = remember { mutableStateOf(value = 48.sp) }
    val sizeSub = remember { mutableStateOf(value = 14.sp) }
    val sizeContent = remember { mutableStateOf(value = 20.sp) }

    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is StartLearnSessionViewModel.UiEvent.ShowSnackbar -> {
                    showSnackbar(
                        context.getString(event.message),
                        SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            FlashWearTopBar(
                state = scaffoldState,
                scope = scope,
                title = "",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            QuestionProgress(startLearnSessionState)

            Divider(Modifier.padding(5.dp))

            QuestionCard(
                startLearnSessionState = startLearnSessionState,
                sizeTitle = sizeTitle,
                sizeContent = sizeContent
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnswerCard(
                startLearnSessionState =  startLearnSessionState,
                sizeTitle =  sizeTitle,
                sizeContent = sizeContent,
                sizeSub = sizeSub
            )

            Spacer(modifier = Modifier.weight(1f))

            UserAnswer(startLearnSessionState, viewModel)

            ReturnTrigger(viewModel, navController)
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ReturnTrigger(
    viewModel: StartLearnSessionViewModel,
    navController: NavController,
) {
    if(viewModel.learnSession.value.finished){

        Log.i("TAG", "learnsession finished DDDDDDDDSAFDSAFAASd")
        navController.popBackStack()

        viewModel.learnSession.value.finished = false
    }
}








