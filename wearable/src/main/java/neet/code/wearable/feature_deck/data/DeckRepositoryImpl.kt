package neet.code.wearable.feature_deck.data

import kotlinx.coroutines.flow.Flow
import neet.code.wearable.feature_deck.data.DeckDao
import neet.code.wearable.feature_deck.domain.Deck
import neet.code.wearable.feature_deck.domain.repository.DeckRepository
import java.util.ArrayList

class DeckRepositoryImpl(
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

    override suspend fun deleteDeckById(deckId: Int) {
        return dao.deleteDeckById(deckId)
    }

    override suspend fun insertDeckOnConflictIgnore(deck: Deck): Long {
        return dao.insertOnConflictIgnore(deck)
    }

    override suspend fun getAllDeckIds(): MutableList<Int> {
        return dao.getAllDeckIds()
    }

    override suspend fun updateDeck(deck: Deck): Int {
        return dao.update(deck)
    }
}