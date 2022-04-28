package neet.code.wearable.feature_deck.domain.use_case

import neet.code.wearable.feature_deck.domain.repository.DeckRepository
import neet.code.wearable.feature_question.domain.repository.QuestionRepository

class DeleteDeck (
    private val repository: DeckRepository,
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(deckId: Int){
        repository.deleteDeckById(deckId)
        questionRepository.deleteQuestionsWithDeck(deckId)
    }
}
