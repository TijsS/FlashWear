package neet.code.flashwear.feature_deck.domain.use_case

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.repository.DeckRepository
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_wearable.WearableUseCases

class DeleteDeck (private val repository: DeckRepository,
                  private val questionRepository: QuestionRepository,
                  private val wearableUseCases: WearableUseCases
) {
    suspend operator fun invoke(deckId: Int) {
        repository.deleteDeckById(deckId)
        questionRepository.deleteQuestionsWithDeck(deckId)
        wearableUseCases.deleteDeckOnWearable(deckId)
    }
}