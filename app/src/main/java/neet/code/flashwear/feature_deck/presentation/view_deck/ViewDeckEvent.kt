package neet.code.flashwear.feature_deck.presentation.view_deck

import neet.code.flashwear.feature_progress.presentation.progress.ProgressGraph
import neet.code.flashwear.feature_progress.presentation.progress.TimeScaleGraph
import neet.code.flashwear.feature_question.domain.model.Question


sealed class ViewDeckEvent {
    data class DeleteQuestion(val question: Question): ViewDeckEvent()
    data class ImportQuestions(val importedQuestions: List<Question>): ViewDeckEvent()
    data class SelectProgressGraph(val selectedGraph: ProgressDeckGraph): ViewDeckEvent()
    data class SelectedTimeScale(val selectedTimeScale: TimeScaleGraph): ViewDeckEvent()

    object SyncWithWearable: ViewDeckEvent()
    object CloseDeleteBox: ViewDeckEvent()
    object ToggleDeleteDeckBox: ViewDeckEvent()
    object DeleteDeck: ViewDeckEvent()
    object RestoreQuestion: ViewDeckEvent()
    object ToggleActionMenu: ViewDeckEvent()
    object ToggleDeleteQuestion: ViewDeckEvent()
}