package neet.code.flashwear.feature_learn_session.domain.use_case

import neet.code.flashwear.feature_deck.presentation.view_deck.TimeScaleGraph
import neet.code.flashwear.feature_learn_session.domain.model.AvgScoreDTO
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository


class GetAvgScoreQuestionsByDeck(
    private val repository: LearnSessionRepository
) {
    suspend operator fun invoke(deckId: Long): MutableMap<TimeScaleGraph, List<AvgScoreDTO>> {
        return repository.getAvgScoreQuestionsByDeck(deckId)
    }
}
