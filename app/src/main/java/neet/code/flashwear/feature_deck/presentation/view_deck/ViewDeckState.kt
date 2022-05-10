package neet.code.flashwear.feature_deck.presentation.view_deck

import com.madrapps.plot.line.DataPoint
import neet.code.flashwear.feature_progress.presentation.progress.TimeScaleGraph
import neet.code.flashwear.feature_question.domain.model.Question
import org.joda.time.LocalDate

data class ViewDeckState (
    val questions: List<Question> = emptyList(),
    val deckId: Int = 0,
    val deckName: String = "",

    val isFloatingMenuVisible: Boolean = false,
    val questionIsHeldForDelete: Boolean = false,
    val selectedTab: DeckCategory = DeckCategory.Questions,
    val categories: List<DeckCategory> = emptyList(),
    val showDeleteDeckBox: Boolean = false,

    var avgScoresLine: Map<TimeScaleGraph, List<DataPoint>> = mapOf(),
    var avgScoreQuestionsLine: Map<TimeScaleGraph, List<DataPoint>> = mapOf(),
    var minutesLearnedLine: Map<TimeScaleGraph, List<DataPoint>> = mapOf(),

    var dateOfFirstLearnSession: LocalDate? = null,
    var selectedProgressGraph: ProgressDeckGraph = ProgressDeckGraph.Score,
    var selectedTimeScaleGraph: TimeScaleGraph = TimeScaleGraph.Day,
)

enum class DeckCategory{
    Questions, Progress
}

enum class ProgressDeckGraph{
    Score, Time, Questions
}

