package neet.code.flashwear.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import neet.code.flashwear.data.FlashWearDatabase
import neet.code.flashwear.feature_deck.data.repository.DeckRepositoryImpl
import neet.code.flashwear.feature_deck.domain.repository.DeckRepository
import neet.code.flashwear.feature_deck.domain.use_case.AddDeck
import neet.code.flashwear.feature_deck.domain.use_case.DecksUseCases
import neet.code.flashwear.feature_deck.domain.use_case.GetDecks
import neet.code.flashwear.feature_learn_session.data.repository.LearnSessionRepositoryImpl
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository
import neet.code.flashwear.feature_learn_session.domain.use_case.LearnSessionUseCases
import neet.code.flashwear.feature_learn_session.domain.use_case.StartLearnSession
import neet.code.flashwear.feature_learn_session.domain.use_case.UpdateLearnSession
import neet.code.flashwear.feature_question.data.repository.QuestionRepositoryImpl
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_question.domain.use_case.*
import neet.code.flashwear.feature_settings.data.repository.SettingsRepositoryImpl
import neet.code.flashwear.feature_settings.domain.model.Settings
import neet.code.flashwear.feature_settings.domain.repository.SettingsRepository
import neet.code.flashwear.feature_settings.domain.use_case.ChangeSettings
import neet.code.flashwear.feature_settings.domain.use_case.GetSettings
import neet.code.flashwear.feature_settings.domain.use_case.SettingsUseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFlashWearDatabase(app: Application): FlashWearDatabase {
        return Room.databaseBuilder(
            app,
            FlashWearDatabase::class.java,
            FlashWearDatabase.DATABASE_NAME
        )   .addCallback(CALLBACK)
            .build()
    }

    private val CALLBACK = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            db.execSQL("INSERT INTO Settings (id, learnStyle) VALUES (1, 'NewWords')")
        }
    }

    //Repostitory's
    @Provides
    @Singleton
    fun provideDeckRepository(db: FlashWearDatabase): DeckRepository {
        return DeckRepositoryImpl(db.deckDao)
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(db: FlashWearDatabase): QuestionRepository {
        return QuestionRepositoryImpl(db.questionDao)
    }

    @Provides
    @Singleton
    fun provideLearnSessionRepository(db: FlashWearDatabase): LearnSessionRepository {
        return LearnSessionRepositoryImpl(db.learnSessionDao)
    }

    @Provides
    @Singleton
    fun provideSettingsRepostitory(db: FlashWearDatabase): SettingsRepository {
        return SettingsRepositoryImpl(db.settingsDao)
    }

    //UseCases
    @Provides
    @Singleton
    fun provideDeckUseCases(repository: DeckRepository): DecksUseCases {
        return DecksUseCases(
            getDecks = GetDecks(repository),
            addDeck = AddDeck(repository),
        )
    }

    @Provides
    @Singleton
    fun provideQuestionUseCases(repository: QuestionRepository, settingsRepository: SettingsRepository): QuestionsUseCases {
        return QuestionsUseCases(
            addQuestion = AddQuestion(repository),
            addQuestions = AddQuestions(repository),
            getQuestionsWithDeckFlow = GetQuestionsWithDeckFlow(repository),
            getQuestionsWithDeck = GetQuestionsWithDeck(repository, settingsRepository),
            deleteQuestion = DeleteQuestion(repository),
            updateQuestion = UpdateQuestion(repository),
            averageScoreOfDeck = AverageScoreOfDeck(repository)
        )
    }

    @Provides
    @Singleton
    fun provideLearnSessionUseCases(repository: LearnSessionRepository): LearnSessionUseCases {
        return LearnSessionUseCases(
            startLearnSession = StartLearnSession(repository),
            updateLearnSession = UpdateLearnSession(repository),
        )
    }

    @Provides
    @Singleton
    fun provideSettingsUseCases(repository: SettingsRepository): SettingsUseCases{
        return SettingsUseCases(
            getSettings = GetSettings(repository),
            changeSettings = ChangeSettings(repository),
        )
    }
}