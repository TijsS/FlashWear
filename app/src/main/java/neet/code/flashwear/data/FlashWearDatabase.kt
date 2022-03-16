package neet.code.flashwear.data

import androidx.room.Database
import androidx.room.RoomDatabase
import neet.code.flashwear.feature_deck.data.data_source.DeckDao
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_learn_session.data.data_source.LearnSessionDao
import neet.code.flashwear.feature_learn_session.domain.model.LearnSession
import neet.code.flashwear.feature_question.data.data_source.QuestionDao
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_settings.data.data_source.SettingsDao
import neet.code.flashwear.feature_settings.domain.model.Settings

@Database(
    entities = [Deck::class, Question::class, LearnSession::class, Settings::class],
    version = 1
)
abstract class FlashWearDatabase: RoomDatabase(){

    abstract val deckDao: DeckDao
    abstract val questionDao: QuestionDao
    abstract val learnSessionDao: LearnSessionDao
    abstract val settingsDao: SettingsDao

    companion object {
        const val DATABASE_NAME = "flashwear_db"
    }
}