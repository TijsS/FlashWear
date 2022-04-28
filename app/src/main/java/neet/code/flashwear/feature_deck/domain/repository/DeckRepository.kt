package neet.code.flashwear.feature_deck.domain.repository

import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.model.WearableDeckDTO

interface DeckRepository {

    fun getDecks(): Flow<List<Deck>>

    suspend fun getDecksForWearableByLastPlayed(): List<WearableDeckDTO>

    suspend fun getDeckById(id: Int): Deck?

    suspend fun insertDeck(deck: Deck): Long

    suspend fun deleteDeck(deck: Deck)

    suspend fun deleteDeckById(deckId: Int)
}