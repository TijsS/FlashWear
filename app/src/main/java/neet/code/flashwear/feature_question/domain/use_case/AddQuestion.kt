package neet.code.flashwear.feature_question.domain.use_case

import neet.code.flashwear.feature_question.domain.model.InvalidQuestionException
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository


class AddQuestion(
    private val repository: QuestionRepository
) {
    @Throws(InvalidQuestionException::class)
    suspend operator fun invoke(question: Question) {
        if(question.deckId?.equals(null) == true){
            throw InvalidQuestionException("Must have deck")
        }
        repository.insertQuestion(question)
    }
}