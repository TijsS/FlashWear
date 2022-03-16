package neet.code.flashwear.feature_deck.presentation.decks

import neet.code.flashwear.feature_deck.domain.util.DeckOrder

sealed class DecksEvent {
    data class Order(val deckOrder: DeckOrder): DecksEvent()
    object ToggleOrderSection: DecksEvent()
    object RandomFloat: DecksEvent()

}