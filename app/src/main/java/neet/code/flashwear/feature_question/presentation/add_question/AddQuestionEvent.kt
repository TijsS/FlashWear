package neet.code.flashwear.feature_question.presentation.add_question

import androidx.compose.ui.focus.FocusState

sealed class AddQuestionEvent {
    data class EnteredQuestionTitle(val value: String): AddQuestionEvent()
    data class EnteredQuestionContent(val value: String): AddQuestionEvent()
    data class EnteredAnswerTitle(val value: String): AddQuestionEvent()
    data class EnteredAnswerSub(val value: String): AddQuestionEvent()
    data class EnteredAnswerContent(val value: String): AddQuestionEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddQuestionEvent()

    object AddQuestion: AddQuestionEvent()
}