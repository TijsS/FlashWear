package neet.code.flashwear.feature_deck.presentation.add_deck

import androidx.compose.ui.focus.FocusState
import neet.code.flashwear.feature_deck.domain.model.Deck

sealed class AddDeckEvent {
    data class EnteredName(val value: String): AddDeckEvent()

    object AddDeck: AddDeckEvent()
}