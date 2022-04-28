package neet.code.flashwear.feature_learn_session.domain.use_case

import android.util.Log
import neet.code.flashwear.feature_learn_session.domain.model.LearnSession
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import neet.code.flashwear.feature_wearable.WearableUseCases

class UpdateLearnSession(
    private val repository: LearnSessionRepository,
    private val wearableUseCases: WearableUseCases,
    private val questionsUseCases: QuestionsUseCases
) {
    suspend operator fun invoke(learnSession: LearnSession) {
        Log.i("TAG", "invoke: update learnsession")
        learnSession.questionsScore = questionsUseCases.averageScoreOfDeck(learnSession.deckId).toDouble()
        repository.updateLearnSession(learnSession)
        wearableUseCases.syncDecks(hardSync = false)
        return
    }
}
