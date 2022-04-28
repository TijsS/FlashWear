package neet.code.wearable.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme


@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = Colors(
    primary = Color(0xFFFF6363),
    primaryVariant = Color(0xFFAE001A),
    secondary = Color(0xFFDA0303),
    onPrimary = Color.White,
    secondaryVariant = Color(0xFFC52121),
    onSecondary = Color(0xFF000000),
    background = Color(0xFF000000),
    surface = Color(0xFF000000),
)

@Composable
fun FlashWearTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        content = content,
    )
}