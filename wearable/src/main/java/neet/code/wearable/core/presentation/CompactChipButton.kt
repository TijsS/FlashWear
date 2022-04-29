package neet.code.wearable.core.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ChipColors
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.Icon


@Composable
fun CompactChipButton(
    drawable: Int,
    colors: ChipColors,
    onClick: () -> Unit

) {

    CompactChip(
        modifier = Modifier
            .padding(bottom = 13.dp)
            .padding(2.dp),
        onClick = { onClick() },
        icon = {
            Icon(
                painter = painterResource(drawable),
                contentDescription = "play icon",
                modifier = Modifier
                    .size(24.dp)
                    .wrapContentSize(align = Alignment.Center),
            )
        },
        colors = colors
    )
}