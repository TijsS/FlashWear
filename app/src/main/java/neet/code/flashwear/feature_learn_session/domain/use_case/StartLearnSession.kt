package neet.code.flashwear.feature_learn_session.domain.use_case

import android.content.ContentValues
import android.util.Log

import neet.code.flashwear.feature_learn_session.domain.model.LearnSession
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import neet.code.flashwear.feature_wearable.WearableUseCases

class StartLearnSession(
    private val repository: LearnSessionRepository,
    private val wearableUseCases: WearableUseCases,
    private val questionsUseCases: QuestionsUseCases
) {
    suspend operator fun invoke(learnSession: LearnSession): Long {

        learnSession.questionsScore = questionsUseCases.averageScoreOfDeck(learnSession.deckId).toDouble()
        return repository.startLearnSession(learnSession)
    }
}