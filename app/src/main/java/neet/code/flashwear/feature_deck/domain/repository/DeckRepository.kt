package neet.code.flashwear.feature_deck.domain.repository

import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_deck.domain.model.Deck

interface DeckRepository {

    fun getDecks(): Flow<List<Deck>>

    suspend fun getDeckById(id: Int): Deck?

    suspend fun insertDeck(deck: Deck)

    suspend fun deleteDeck(deck: Deck)
}