package neet.code.wearable.feature_deck.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import neet.code.wearable.feature_deck.domain.Deck
import neet.code.wearable.feature_deck.domain.repository.DeckRepository


class GetDecks(
    private val repository: DeckRepository
) {
    operator fun invoke(): Flow<List<Deck>> {
        return repository.getDecks()
    }
}
