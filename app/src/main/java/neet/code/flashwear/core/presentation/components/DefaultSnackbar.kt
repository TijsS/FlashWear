package neet.code.flashwear.core.presentation.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { }
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.body2
                    )

                },
                action = {
                    data.actionLabel?.let { actionLabel ->
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = actionLabel,
                                color= MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}

class SnackbarDemoAppState(
    val scaffoldState: ScaffoldState,
    val snackbarScope: CoroutineScope,
    val navController: NavHostController,
    val context: Context
) {
    fun showSnackbar(message: String, actionLabel: Int, duration: SnackbarDuration = SnackbarDuration.Long) {
        snackbarScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                duration = duration,
                actionLabel = context.getString(actionLabel)
            )
        }
    }
}

@Composable
fun rememberSnackbarDemoAppState(
    context: Context,
    scaffoldState: ScaffoldState = rememberScaffoldState(
        snackbarHostState = remember {
            SnackbarHostState()
        }
    ),
    navController: NavHostController = rememberNavController(),
    snackbarScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, navController, snackbarScope) {

    SnackbarDemoAppState(
        context = context,
        scaffoldState = scaffoldState,
        navController = navController,
        snackbarScope = snackbarScope
    )
}