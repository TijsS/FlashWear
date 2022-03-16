package neet.code.flashwear.feature_deck.domain.use_case

import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.model.InvalidDeckException
import neet.code.flashwear.feature_deck.domain.repository.DeckRepository

class AddDeck(
    private val repository: DeckRepository
) {
    @Throws(InvalidDeckException::class)
    suspend operator fun invoke(deck: Deck) {
        if(deck.name.isBlank()){
            throw InvalidDeckException("Name of deck can not be empty")
        }
        if(deck.name.length > 36){
            throw InvalidDeckException("Name of deck is too long")
        }
        if(deck.questions.isEmpty()){
            throw InvalidDeckException("Deck needs at least one answer")
        }
        repository.insertDeck(deck)
    }
}