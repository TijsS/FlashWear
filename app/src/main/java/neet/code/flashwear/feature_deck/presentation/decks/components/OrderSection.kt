package neet.code.flashwear.feature_deck.presentation.decks.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import neet.code.flashwear.feature_deck.domain.util.DeckOrder
import neet.code.flashwear.feature_deck.domain.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    deckOrder: DeckOrder = DeckOrder.Date(OrderType.Descending),
    onOrderChange: (DeckOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Name",
                selected = deckOrder is DeckOrder.Name,
                onSelect = { onOrderChange(DeckOrder.Name(deckOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Date",
                selected = deckOrder is DeckOrder.Date,
                onSelect = { onOrderChange(DeckOrder.Date(deckOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "LastPlayed",
                selected = deckOrder is DeckOrder.LastPlayed,
                onSelect = { onOrderChange(DeckOrder.LastPlayed(deckOrder.orderType)) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = deckOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(deckOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = deckOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(deckOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}