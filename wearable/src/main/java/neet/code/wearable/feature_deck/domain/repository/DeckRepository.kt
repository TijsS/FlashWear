package neet.code.wearable.feature_deck.domain.repository

import kotlinx.coroutines.flow.Flow
import neet.code.wearable.feature_deck.domain.Deck

interface DeckRepository {

    fun getDecks(): Flow<List<Deck>>

    suspend fun getDeckById(id: Int): Deck?

    suspend fun insertDeck(deck: Deck)

    suspend fun insertDeckOnConflictIgnore(deck: Deck): Long

    suspend fun getAllDeckIds(): MutableList<Int>

    suspend fun updateDeck(deck: Deck): Int

    suspend fun deleteDeck(deck: Deck)

    suspend fun deleteDeckById(deckId: Int)
}