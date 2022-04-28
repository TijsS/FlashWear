package neet.code.flashwear.feature_deck.domain.use_case

import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.model.InvalidDeckException
import neet.code.flashwear.feature_deck.domain.repository.DeckRepository
import neet.code.flashwear.feature_learn_session.domain.model.LearnSession
import neet.code.flashwear.feature_learn_session.domain.use_case.LearnSessionUseCases
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import neet.code.flashwear.feature_wearable.SyncDecks
import neet.code.flashwear.feature_wearable.WearableUseCases
import javax.inject.Inject

@AndroidEntryPoint
class AddDeck(
    private val repository: DeckRepository,
    private val wearableUseCases: WearableUseCases,
    private val learnSessionUseCases: LearnSessionUseCases
): ComponentActivity() {

    @Throws(InvalidDeckException::class)
    suspend operator fun invoke(deck: Deck) {
        if(deck.name.isBlank()){
            throw InvalidDeckException("Name of deck can not be empty")
        }
        if(deck.name.length > 36){
            throw InvalidDeckException("Name of deck is too long")
        }
        val deckId = repository.insertDeck(deck)
        learnSessionUseCases.startLearnSession(
            LearnSession(
            score = 0.0,
            timeStart = System.currentTimeMillis() - 24*60*60*1000,
            timeEnd = System.currentTimeMillis() - 24*60*60*1000,
            deckId = deckId.toInt()
        ))
        wearableUseCases.syncDecks(hardSync = false)
    }
}