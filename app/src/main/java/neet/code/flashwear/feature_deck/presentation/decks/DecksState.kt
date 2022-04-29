package neet.code.flashwear.feature_deck.presentation.decks

import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.util.DeckOrder
import neet.code.flashwear.feature_deck.domain.util.OrderType

data class DecksState (
    val decks: List<Deck>? = null,
    val deckOrder: DeckOrder = DeckOrder.LastPlayed(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val averageScoreOfDecks: MutableMap<Int?, Float?> = mutableMapOf(),
    val isFloatingMenuVisible: Boolean = false,
)

