package neet.code.flashwear.feature_deck.presentation.add_deck

sealed class AddDeckEvent {
    data class EnteredName(val value: String): AddDeckEvent()

    object AddDeck: AddDeckEvent()
}