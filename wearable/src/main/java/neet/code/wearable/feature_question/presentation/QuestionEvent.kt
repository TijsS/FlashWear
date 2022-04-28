package neet.code.wearable.feature_question.presentation


sealed class QuestionEvent {
    data class NextQuestion(val value: String): QuestionEvent()

    object ShowAnswer: QuestionEvent()
}