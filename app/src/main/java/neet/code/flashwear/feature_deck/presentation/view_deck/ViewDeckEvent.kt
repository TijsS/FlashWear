package neet.code.flashwear.feature_deck.presentation.view_deck

import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_question.domain.model.Question


sealed class ViewDeckEvent {
    data class DeleteQuestion(val question: Question): ViewDeckEvent()
    data class ImportQuestions(val importedQuestions: List<Question>): ViewDeckEvent()

    object RestoreQuestion: ViewDeckEvent()
    object ToggleActionMenu: ViewDeckEvent()
    object ToggleDeleteQuestion: ViewDeckEvent()
}