package neet.code.flashwear.feature_question.domain.use_case

import android.content.ContentValues
import android.util.Log
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_wearable.WearableUseCases

class UpdateQuestion(
    private val repository: QuestionRepository,
    private val wearableUseCases: WearableUseCases
) {
    suspend operator fun invoke(question: Question) {
        Log.i(ContentValues.TAG, "update: ${question.questionTitle}")
        repository.updateQuestion(question)
        Log.i(ContentValues.TAG, "question updated")
        wearableUseCases.syncQuestion(question)
    }
}