package neet.code.flashwear.feature_deck.presentation.view_deck

import kotlinx.coroutines.flow.MutableStateFlow
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.util.DeckOrder
import neet.code.flashwear.feature_deck.domain.util.OrderType
import neet.code.flashwear.feature_question.domain.model.Question

data class ViewDeckState (
    val questions: List<Question> = emptyList(),
    val deckId: Int = 0,
    val isFloatingMenuVisible: Boolean = false,
    val questionIsHeldForDelete: Boolean = false,
    val selectedTab: DeckQuestionCategory = DeckQuestionCategory.Questions,
    val categories: List<DeckQuestionCategory> = emptyList(),
    val showImportedQuestionsMessage: Boolean = false,
    val importedQuestionsMessage: String = ""
)

enum class DeckQuestionCategory{
    Questions, Progress
}