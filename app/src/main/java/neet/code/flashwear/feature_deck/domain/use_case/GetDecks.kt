package neet.code.flashwear.feature_deck.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.repository.DeckRepository
import neet.code.flashwear.feature_deck.domain.util.DeckOrder
import neet.code.flashwear.feature_deck.domain.util.OrderType

class GetDecks(
    private val repository: DeckRepository
) {

    operator fun invoke(
        deckOrder: DeckOrder = DeckOrder.LastPlayed(OrderType.Descending)
    ): Flow<List<Deck>> {
        return repository.getDecks().map{ decks ->
            when(deckOrder.orderType) {
                is OrderType.Ascending -> {
                    when(deckOrder){
                        is DeckOrder.Name -> decks.sortedBy { it.name.lowercase() }
                        is DeckOrder.Date -> decks.sortedBy { it.created }
                        is DeckOrder.LastPlayed -> decks.sortedBy { it.lastPlayed }

                    }
                }
                is OrderType.Descending ->{
                    when(deckOrder){
                        is DeckOrder.Name -> decks.sortedByDescending { it.name.lowercase() }
                        is DeckOrder.Date -> decks.sortedByDescending { it.created }
                        is DeckOrder.LastPlayed -> decks.sortedByDescending { it.lastPlayed }

                    }
                }
            }

        }
    }
}