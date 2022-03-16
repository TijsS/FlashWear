package neet.code.flashwear.feature_learn_session.domain.use_case

import neet.code.flashwear.feature_learn_session.domain.model.LearnSession
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository

class UpdateLearnSession(
    private val repository: LearnSessionRepository,
) {
    suspend operator fun invoke(learnSession: LearnSession) {
        return repository.updateLearnSession(learnSession)
    }
}
