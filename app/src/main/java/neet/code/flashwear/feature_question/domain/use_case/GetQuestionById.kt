package neet.code.flashwear.feature_question.domain.use_case

import android.content.ContentValues
import android.util.Log
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_wearable.WearableUseCases

class GetQuestionById(
    private val repository: QuestionRepository
) {
    suspend operator fun invoke(questionId: Int): Question? {
        return repository.getQuestionById(questionId)
    }
}