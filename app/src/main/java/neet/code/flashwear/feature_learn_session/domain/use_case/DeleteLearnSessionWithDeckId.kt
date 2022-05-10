package neet.code.flashwear.feature_learn_session.domain.use_case

import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository


class DeleteLearnSessionWithDeckId(
    private val repository: LearnSessionRepository,
) {
    suspend operator fun invoke(deckId: Int) {
        repository.deleteLearnSessionWithDeckId(deckId)
    }
}