package neet.code.flashwear.feature_question.domain.use_case

import neet.code.flashwear.feature_question.domain.model.InvalidQuestionException
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository

class AddQuestions(
    private val repository: QuestionRepository
) {
    @Throws(InvalidQuestionException::class)
    suspend operator fun invoke(question: List<Question>) {
        repository.insertQuestions(question)
    }
}