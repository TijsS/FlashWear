package neet.code.flashwear.core.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import neet.code.flashwear.R
import neet.code.flashwear.Screen

@Composable
fun FlashWearDrawer(navController: NavController, scope: CoroutineScope) {
    val scaffoldState = rememberScaffoldState()

    Text(stringResource(R.string.app_name), modifier = Modifier.padding(16.dp) )

    Spacer(modifier = Modifier.height(15.dp))
    Divider(thickness = 1.5.dp)
    Spacer(modifier = Modifier.height(5.dp))


    DrawerItem(
        scope = scope,
        scaffoldState = scaffoldState,
        navController = navController,
        screen = Screen.DecksScreen.route,
        icon = Icons.Filled.School,
        iconDescription = stringResource(R.string.decks)
    )

    DrawerItem(
        scope = scope,
        scaffoldState = scaffoldState,
        navController = navController,
        screen = Screen.ProgressScreen.route,
        icon = Icons.Filled.PieChart,
        iconDescription = stringResource(R.string.progress)
    )

    DrawerItem(
        scope = scope,
        scaffoldState = scaffoldState,
        navController = navController,
        screen = Screen.SettingsScreen.route,
        icon = Icons.Filled.Settings,
        iconDescription = stringResource(R.string.settings)
    )
}

@Composable
fun DrawerItem(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController,
    screen: String,
    icon: ImageVector,
    iconDescription: String,
){
    Button(
        onClick = {
            navController.navigate(screen)
            scope.launch { scaffoldState.drawerState.close() }
        },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(iconDescription)
    }

    Spacer(modifier = Modifier.height(5.dp))
    Divider(thickness = 0.8.dp)
    Spacer(modifier = Modifier.height(5.dp))
}