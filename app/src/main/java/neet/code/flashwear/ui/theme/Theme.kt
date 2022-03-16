package neet.code.flashwear.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = Color(0xFFFF6363),
    primaryVariant = Color(0xFFAE001A),
    secondary = Color(0xFFDA0303),
    onPrimary = Color.White,
    secondaryVariant = Color(0xFFC52121),
    onSecondary = Color(0xFF3D3F4D),
    background = Color(0xFF2C2F3C),
    surface = Color(0xFF31333F),
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = Color(0xFFEB4C4C),
    primaryVariant = Color(0xFFBC1010),
    secondary = Color(0xFFD30606),
    onPrimary = Color.Black,
    secondaryVariant = Color(0xFFC52121),
    onSecondary = Color(0xFFDDDDDD),
    surface = Color(0xFFFFFFFF),
    )

@Composable
fun FlashWearTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}