package neet.code.wearable.feature_question.domain.use_case

import neet.code.wearable.feature_question.domain.Question
import neet.code.wearable.feature_question.domain.repository.QuestionRepository


class DeleteQuestion(
    private val repository: QuestionRepository
) {
    suspend operator fun invoke(questionId: Int){
        repository.deleteQuestionById(questionId)
    }
}
