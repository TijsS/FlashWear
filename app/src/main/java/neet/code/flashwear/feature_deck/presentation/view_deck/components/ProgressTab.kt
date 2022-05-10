package neet.code.flashwear.feature_deck.presentation.view_deck.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import neet.code.flashwear.feature_deck.presentation.view_deck.ProgressDeckGraph
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckViewModel
import neet.code.flashwear.feature_progress.presentation.progress.ProgressGraph
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProgressTab(
    viewModel: ViewDeckViewModel = hiltViewModel()
){
    val totalWidth = remember { mutableStateOf(0) }
    Column(Modifier.onGloballyPositioned {
        totalWidth.value = it.size.width
    }) {
        DeckGraphSelectButtons(viewModel)

        val xOffset = remember { mutableStateOf(0f) }
        val cardWidth = remember { mutableStateOf(0) }
        val visibility = remember { mutableStateOf(false) }
        val points = remember { mutableStateOf(listOf<DataPoint>()) }
        val density = LocalDensity.current


        Box(Modifier.height(30.dp)) {
            if (visibility.value) {
                Surface(
                    modifier = Modifier
                        .width(140.dp)
                        .align(Alignment.BottomCenter)
                        .onGloballyPositioned {
                            cardWidth.value = it.size.width
                        }
                        .graphicsLayer(translationX = xOffset.value),
                    shape = RoundRectangle,
                    color = MaterialTheme.colors.onSecondary
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val value = points.value
                        if (value.isNotEmpty()) {
                            val scoreDate = viewModel.getDateForX(value.first().x.toInt())
                            Row(){
                                if (scoreDate != null) {
                                    Text(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        text = scoreDate,
                                        style = MaterialTheme.typography.caption,
                                        fontSize = MaterialTheme.typography.caption.fontSize,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.size(10.dp))
                                Text(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    text = "${value.first().y}${viewModel.getGraphSymbol()}",
                                    style = MaterialTheme.typography.caption,
                                    fontSize = MaterialTheme.typography.caption.fontSize,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        val padding = 16.dp
        val ySteps = 2
        val xSteps = 1

        LineGraph(
            plot = LinePlot(
                lines = getSelectedLine(viewModel),
                selection = LinePlot.Selection(highlight = null),
                horizontalExtraSpace = 12.dp,
                yAxis = LinePlot.YAxis(
                    steps = ySteps,
                    paddingStart = 6.dp,
                    content = { min, offset, max ->
                        for (it in 0 until ySteps) {
                            val value = it * offset + min
                            Text(
                                text = "${value.roundToInt()}${viewModel.getGraphSymbol()}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                ),
                xAxis = LinePlot.XAxis(
                    steps = xSteps,
                    content =  { min, offset, _ ->
                        for (it in 0 until xSteps) {
                            val value = it * offset + min
                            viewModel.getDateForX(value.toInt())?.let { it1 ->
                                Text(
                                    text = it1,
                                    maxLines = 1,
                                    overflow = TextOverflow.Clip,
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.onSurface,
                                    modifier = Modifier.padding(start = 30.dp, end = 2.dp),
                                )
                            }
                        }
                    }
                )
            ),

            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = padding),
            onSelectionStart = { visibility.value = true },
            onSelectionEnd = { visibility.value = false }
        ) { x, pts ->
            val cWidth = cardWidth.value.toFloat()
            var xCenter = x + padding.toPx(density)
            xCenter = when {
                xCenter + cWidth / 2f > totalWidth.value -> totalWidth.value - cWidth
                xCenter - cWidth / 2f < 0f -> 0f
                else -> xCenter - cWidth / 2f
            }
            xOffset.value = xCenter
            points.value = pts
        }
    }
}

val RoundRectangle: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline.Rounded {
        val radius = 8.dp.value * density.density
        return Outline.Rounded(RoundRect(size.toRect(), CornerRadius(radius, radius)))
    }

    override fun toString(): String = "RoundRectangleShape"
}

@Composable
private fun getSelectedLine(viewModel: ViewDeckViewModel): MutableList<LinePlot.Line> {
    val circleColor = MaterialTheme.colors.primary
    val highlightCircleColor = MaterialTheme.colors.primaryVariant

    val line = mutableListOf<LinePlot.Line>()
    val selectedProgressGraph = viewModel.viewDeckState.value.selectedProgressGraph
    val selectedTimeScale = viewModel.viewDeckState.value.selectedTimeScaleGraph

    if(selectedProgressGraph == ProgressDeckGraph.Score){
        line.add(
            makeLine(
                dataPoints = viewModel.viewDeckState.value.avgScoresLine[selectedTimeScale]!!,
                circleColor = circleColor,
                highlightCircleColor = highlightCircleColor
            )
        )
    }

    if(selectedProgressGraph == ProgressDeckGraph.Time) {
        line.add(
            makeLine(
                dataPoints = viewModel.viewDeckState.value.minutesLearnedLine[selectedTimeScale]!!,
                circleColor = circleColor,
                highlightCircleColor = highlightCircleColor
            )
        )
    }

    if(selectedProgressGraph == ProgressDeckGraph.Questions) {
        line.add(
            makeLine(
                dataPoints = viewModel.viewDeckState.value.avgScoreQuestionsLine[selectedTimeScale]!!,
                circleColor = circleColor,
                highlightCircleColor = highlightCircleColor
            )
        )
    }
    return line
}


private fun makeLine(
    dataPoints: List<DataPoint>,
    circleColor: Color,
    highlightCircleColor: Color,
): LinePlot.Line{
    return LinePlot.Line(
        dataPoints = dataPoints,
        connection =  LinePlot.Connection(
            color = circleColor
        ),
        intersection = LinePlot.Intersection(
            color = circleColor
        ),
        highlight = LinePlot.Highlight { center ->
            drawCircle(Color.Black, 9.dp.toPx(), center, 0.2f)
            drawCircle(Color.Black, 10.dp.toPx(), center, 0.1f)
            drawCircle(highlightCircleColor, 7.dp.toPx(), center)
        },
        areaUnderLine = LinePlot.AreaUnderLine(
            color = circleColor,
            alpha = 0.2f,
        ),
    )
}

internal fun Dp.toPx(density: Density) = value * density.density
