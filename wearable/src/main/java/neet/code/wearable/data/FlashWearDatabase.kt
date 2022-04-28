package neet.code.wearable.data

import androidx.room.Database
import androidx.room.RoomDatabase
import neet.code.wearable.feature_deck.data.DeckDao
import neet.code.wearable.feature_deck.domain.Deck
import neet.code.wearable.feature_question.data.QuestionDao
import neet.code.wearable.feature_question.domain.Question


@Database(
    entities = [Deck::class, Question::class],
    version = 1
)
abstract class FlashWearDatabase: RoomDatabase(){

    abstract val deckDao: DeckDao
    abstract val questionDao: QuestionDao

    companion object {
        const val DATABASE_NAME = "flashwear_db"
    }
}