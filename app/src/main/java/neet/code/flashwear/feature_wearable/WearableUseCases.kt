package neet.code.flashwear.feature_wearable

import neet.code.flashwear.feature_deck.domain.use_case.AddDeck
import neet.code.flashwear.feature_deck.domain.use_case.GetDecks
import neet.code.flashwear.feature_deck.domain.use_case.GetDecksForWearable
import neet.code.flashwear.feature_question.domain.use_case.UpdateWearableScore

data class WearableUseCases (
    val syncDecks: SyncDecks,
    val syncQuestions: SyncQuestions,
    val syncQuestion: SyncQuestion,
    val deleteQuestionOnWearable: DeleteQuestionOnWearable,
    val deleteDeckOnWearable: DeleteDeckOnWearable,
    ){

}