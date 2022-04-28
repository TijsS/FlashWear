package neet.code.flashwear.ui.progress

import androidx.compose.foundation.layout.*
import androidx.compose.material.SnackbarDuration
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@Composable
fun ProgressBody(navController: NavController,
                 showSnackbar: (String, SnackbarDuration) -> Unit,
                 viewModel: s = hiltViewModel()
){
    Column() {
    }
}