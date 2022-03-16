package neet.code.flashwear.feature_question.domain.use_case

import android.content.ContentValues
import android.util.Log
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository

class UpdateQuestion (
    private val repository: QuestionRepository
) {
    suspend operator fun invoke(question: Question) {
        Log.i(ContentValues.TAG, "update: ${question.questionTitle}")
        repository.updateQuestion(question)
    }
}