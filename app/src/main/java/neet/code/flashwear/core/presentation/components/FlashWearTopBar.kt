package neet.code.flashwear.core.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import neet.code.flashwear.ui.theme.DarkBlueGray


@Composable
fun FlashWearTopBar(
    state: ScaffoldState,
    scope: CoroutineScope,
    title: String,
    withFunction: Boolean,
    actionFunction: (() -> Unit)? = null,
    icon: ImageVector? = null
){

    if(withFunction) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch { state.drawerState.open() }
                })
                {
                    Icon(Icons.Filled.Menu, contentDescription = null)
                }
            },
            actions = {
                // RowScope here, so these icons will be placed horizontally
                IconButton(onClick = { actionFunction?.invoke() }) {
                    if (icon != null) {
                        Icon(imageVector = icon, contentDescription = "")
                    }
                }
            },
            backgroundColor = if (isSystemInDarkTheme()) DarkBlueGray else Color.White,
            contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
    else{
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch { state.drawerState.open() }
                })
                {
                    Icon(Icons.Filled.Menu, contentDescription = null)
                }
            },
            backgroundColor = if (isSystemInDarkTheme()) DarkBlueGray else Color.White,
            contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}