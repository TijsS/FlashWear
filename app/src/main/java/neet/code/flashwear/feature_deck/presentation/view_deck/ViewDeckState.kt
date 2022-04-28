package neet.code.flashwear.feature_deck.presentation.view_deck

import com.madrapps.plot.line.DataPoint
import neet.code.flashwear.feature_question.domain.model.Question
import org.joda.time.LocalDate

data class ViewDeckState (
    val questions: List<Question> = emptyList(),
    val deckId: Int = 0,
    val deckName: String = "",

    val isFloatingMenuVisible: Boolean = false,
    val questionIsHeldForDelete: Boolean = false,
    val selectedTab: DeckQuestionCategory = DeckQuestionCategory.Questions,
    val categories: List<DeckQuestionCategory> = emptyList(),
    val showImportedQuestionsMessage: Boolean = false,
    val importedQuestionsMessage: String = "",

    val avgScorePerX: List<DataPoint> = emptyList(),
    val avgScoreQuestionsPerX: List<DataPoint> = emptyList(),
    val minutesSpendPerX: List<DataPoint> = emptyList(),
    var dateOfFirstLearnSession: LocalDate? = null,
    var selectedProgressGraph: ProgressGraph = ProgressGraph.Score,
    var selectedTimeScaleGraph: TimeScaleGraph = TimeScaleGraph.Day,
)

enum class DeckQuestionCategory{
    Questions, Progress
}

enum class ProgressGraph{
    Score, Time, Questions
}

enum class TimeScaleGraph{
    Day, Week, Month
}