package neet.code.flashwear.feature_deck.data.repository

import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_deck.data.data_source.DeckDao
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.repository.DeckRepository
import java.util.ArrayList

class DeckRepositoryImpl (
    private val dao: DeckDao
        ) : DeckRepository {

    override fun getDecks(): Flow<List<Deck>> {
        return dao.getAll()
    }

    override suspend fun getDeckById(id: Int): Deck? {
        return dao.getDeckById(id)
    }

    override suspend fun deleteDeck(deck: Deck) {
        return dao.delete(deck)
    }

    override suspend fun insertDeck(deck: Deck) {
        return dao.insert(deck)
    }
}