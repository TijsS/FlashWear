package neet.code.flashwear.feature_deck.domain.use_case

import neet.code.flashwear.feature_deck.domain.repository.DeckRepository
import neet.code.flashwear.feature_learn_session.domain.use_case.LearnSessionUseCases
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_wearable.WearableUseCases

class DeleteDeck (private val repository: DeckRepository,
                  private val questionRepository: QuestionRepository,
                  private val learnSessionRepository: LearnSessionUseCases,
                  private val wearableUseCases: WearableUseCases
) {

    suspend operator fun invoke(deckId: Int) {
        repository.deleteDeckById(deckId)
        questionRepository.deleteQuestionsWithDeck(deckId)
        learnSessionRepository.deleteLearnSessionWithDeckId(deckId)
        wearableUseCases.deleteDeckOnWearable(deckId)
    }
}