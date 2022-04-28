package neet.code.flashwear.feature_learn_session.domain.use_case

import neet.code.flashwear.feature_deck.presentation.view_deck.TimeScaleGraph
import neet.code.flashwear.feature_learn_session.domain.model.MinutesLearnedDTO
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository

class GetMinutesLearnedByDeck(
    private val repository: LearnSessionRepository
){
    suspend operator fun invoke(deckId: Long): MutableMap<TimeScaleGraph, List<MinutesLearnedDTO>> {
        return repository.getMinutesLearnedByDeck(deckId)
    }
}