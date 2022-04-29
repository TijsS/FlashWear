package neet.code.flashwear.feature_question.domain.use_case

import android.content.ContentValues.TAG
import android.util.Log
import dagger.hilt.internal.aggregatedroot.codegen._neet_code_flashwear_FlashWearApplication
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_wearable.WearableUseCases

class DeleteQuestion(
    private val repository: QuestionRepository,
    private val wearableUseCases: WearableUseCases
) {
    suspend operator fun invoke(question: Question) {
        Log.i(TAG, "invoke: ${question.questionTitle}")
        repository.deleteQuestion(question)
        question.id?.let { wearableUseCases.deleteQuestionOnWearable(it) }
    }
}