package neet.code.flashwear.feature_deck.presentation.add_deck

import androidx.compose.ui.focus.FocusState
import neet.code.flashwear.feature_deck.domain.model.Deck

sealed class AddDeckEvent {
    data class EnteredName(val value: String): AddDeckEvent()
    data class EnteredQuestion(val value: String): AddDeckEvent()
    data class EnteredAnswer(val value: String): AddDeckEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddDeckEvent()

    object AddDeck: AddDeckEvent()
}