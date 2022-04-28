package neet.code.flashwear.feature_deck.data.repository

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import neet.code.flashwear.feature_deck.data.data_source.DeckDao
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.model.WearableDeckDTO
import neet.code.flashwear.feature_deck.domain.repository.DeckRepository

class DeckRepositoryImpl (
    private val dao: DeckDao
        ) : DeckRepository {

    @OptIn(FlowPreview::class)
    override fun getDecks(): Flow<List<Deck>> {
        return dao.getDecksByLastPlayedFlow()
    }

    override suspend fun getDecksForWearableByLastPlayed(): List<WearableDeckDTO> {
        return dao.getDecksForWearableByLastPlayed()
    }

    override suspend fun getDeckById(id: Int): Deck? {
        return dao.getDeckById(id)
    }

    override suspend fun deleteDeck(deck: Deck) {
        return dao.delete(deck)
    }

    override suspend fun deleteDeckById(deckId: Int) {
        return dao.deleteById(deckId)
    }

    override suspend fun insertDeck(deck: Deck): Long {
        return dao.insert(deck)
    }
}