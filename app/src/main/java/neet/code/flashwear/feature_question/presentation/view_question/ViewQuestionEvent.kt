package neet.code.flashwear.feature_question.presentation.view_question

sealed class ViewQuestionEvent {
    data class EnteredQuestionTitle(val value: String): ViewQuestionEvent()
    data class EnteredQuestionContent(val value: String): ViewQuestionEvent()
    data class EnteredAnswerTitle(val value: String): ViewQuestionEvent()
    data class EnteredAnswerSub(val value: String): ViewQuestionEvent()
    data class EnteredAnswerContent(val value: String): ViewQuestionEvent()
    data class EnteredScore(val value: String): ViewQuestionEvent()

    object UpdateQuestion: ViewQuestionEvent()
}