package neet.code.flashwear.feature_question.domain.use_case

import android.content.ContentValues.TAG
import android.util.Log
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.StartLearnSessionViewModel
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_wearable.WearableUseCases
import java.lang.Double.max
import java.lang.Double.min

class UpdateWearableScore(
    private val repository: QuestionRepository,
    private val wearableUseCases: WearableUseCases
) {

    suspend operator fun invoke(questionResult: String, questionId: Int) {
        val question = repository.getQuestionById(questionId)

        var newScore = question?.score
        if(question == null){
            Log.i(TAG, "question of new wearable score not found")
            return
        }
        when(questionResult){
            "+" -> {
                //score = score * 115% (add 15% to the score)
                newScore = question.score?.times(StartLearnSessionViewModel.CORRECT_MODIFIER)
                    ?.let { min(it.round(), 1.0) }!!
            }
            "=" -> {
                //score = score * 95% (subtract 5% from the score)
                newScore = question.score?.times(StartLearnSessionViewModel.HALF_CORRECT_MODIFIER)?.round()
                    ?.let { max(it, 0.0999) }!!
            }
            "-" -> {
                //score = score * 80% (subtract 20% from the score)
                newScore = question.score?.times(StartLearnSessionViewModel.WRONG_MODIFIER)?.round()
                    ?.let { max(it, 0.0999) }!!
            }
        }
        question.score = newScore

        repository.updateQuestion(question)
        wearableUseCases.syncQuestion(question)
    }

    private fun Double.round(): Double = String.format("%.3f", this).toDouble()
}