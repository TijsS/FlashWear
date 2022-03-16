package neet.code.flashwear.feature_learn_session.domain.use_case

import android.content.ContentValues
import android.util.Log

import neet.code.flashwear.feature_learn_session.domain.model.LearnSession
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository

class StartLearnSession (
    private val repository: LearnSessionRepository,
) {
    suspend operator fun invoke(learnSession: LearnSession): Long {
        return repository.startLearnSession(learnSession)
    }
}