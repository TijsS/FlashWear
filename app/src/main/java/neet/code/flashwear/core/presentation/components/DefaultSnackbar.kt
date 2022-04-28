package neet.code.flashwear.core.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val navController: NavHostController
) {
    fun showSnackbar(message: String, actionLabel: String, duration: SnackbarDuration = SnackbarDuration.Long) {
        snackbarScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                duration = duration,
                actionLabel = actionLabel
            )
        }
    }
}

@Composable
fun rememberSnackbarDemoAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(
        snackbarHostState = remember {
            SnackbarHostState()
        }
    ),
    navController: NavHostController = rememberNavController(),
    snackbarScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, navController, snackbarScope) {
    SnackbarDemoAppState(
        scaffoldState = scaffoldState,
        navController = navController,
        snackbarScope = snackbarScope
    )
}