package neet.code.wearable.feature_deck.domain.use_case

import neet.code.wearable.feature_deck.domain.Deck
import neet.code.wearable.feature_deck.domain.repository.DeckRepository
import neet.code.wearable.feature_question.domain.repository.QuestionRepository

class SyncDecks(
    private val deckRepository: DeckRepository,
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(decks: List<Deck>, hardSync: Boolean){

        val oldDecks = deckRepository.getAllDeckIds()

        for (deck in decks){
            //insert deck, if already exist Conflict.ignore returns -1. in this case update deck
            val result = deckRepository.insertDeckOnConflictIgnore(deck)
            if(result == -1L){
                oldDecks.remove(deck.id)
                deckRepository.updateDeck(deck)
            }
        }

        //decks are not on phone anymore, remove these
        for(deckId in oldDecks){
            deckRepository.deleteDeckById(deckId)
            questionRepository.deleteQuestionsWithDeck(deckId)
        }
    }
}
