package neet.code.flashwear.feature_question.domain.use_case

import android.content.ContentValues.TAG
import android.util.Log
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository

class DeleteQuestion (
    private val repository: QuestionRepository
) {
    suspend operator fun invoke(question: Question) {
        Log.i(TAG, "invoke: ${question.questionTitle}")
        repository.deleteQuestion(question)
    }
}