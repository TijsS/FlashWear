package neet.code.flashwear.feature_deck.presentation.add_deck

import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.util.DeckOrder
import neet.code.flashwear.feature_deck.domain.util.OrderType

data class AddDecksState (
    val text: String = "",
    val hint: String = ""
)