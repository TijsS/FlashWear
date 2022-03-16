package neet.code.flashwear.feature_deck.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import neet.code.flashwear.feature_question.domain.model.Question

data class DeckWithQuestions(
    @Embedded val deck: Deck,
    @Relation(
        parentColumn = "id",
        entityColumn = "deckId"
    )
    val questions: List<Question>
)
