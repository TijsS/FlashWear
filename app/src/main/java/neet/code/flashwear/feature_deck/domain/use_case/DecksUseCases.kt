package neet.code.flashwear.feature_deck.domain.use_case

data class DecksUseCases (
    val getDecks: GetDecks,
    val getDecksForWearable: GetDecksForWearable,
    val addDeck: AddDeck,
    val deleteDeck: DeleteDeck
    )
