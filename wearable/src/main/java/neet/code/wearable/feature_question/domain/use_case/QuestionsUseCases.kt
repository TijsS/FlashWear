package neet.code.wearable.feature_question.domain.use_case


data class QuestionsUseCases (
    val getQuestionsWithDeck: GetQuestionsWithDeck,
    val sendQuestionResult: SendQuestionResult,
    val syncQuestions: SyncQuestions,
    val syncQuestion: SyncQuestion,
    val deleteQuestion: DeleteQuestion,
)
