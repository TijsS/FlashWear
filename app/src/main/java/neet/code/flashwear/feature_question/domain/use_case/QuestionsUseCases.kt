package neet.code.flashwear.feature_question.domain.use_case

data class QuestionsUseCases (
    val getQuestionsWithDeckFlow: GetQuestionsWithDeckFlow,
    val getQuestionsWithDeck: GetQuestionsWithDeck,
    val getQuestionById: GetQuestionById,
    val averageScoreOfDeck: AverageScoreOfDeck,
    val addQuestion: AddQuestion,
    val addQuestions: AddQuestions,
    val deleteQuestion: DeleteQuestion,
    val updateQuestion: UpdateQuestion,
    val updateWearableScore: UpdateWearableScore,
    )
