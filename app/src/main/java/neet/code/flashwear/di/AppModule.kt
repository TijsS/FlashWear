package neet.code.flashwear.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import neet.code.flashwear.data.FlashWearDatabase
import neet.code.flashwear.feature_deck.data.repository.DeckRepositoryImpl
import neet.code.flashwear.feature_deck.domain.repository.DeckRepository
import neet.code.flashwear.feature_deck.domain.use_case.*
import neet.code.flashwear.feature_learn_session.data.repository.LearnSessionRepositoryImpl
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository
import neet.code.flashwear.feature_learn_session.domain.use_case.*
import neet.code.flashwear.feature_question.data.repository.QuestionRepositoryImpl
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository
import neet.code.flashwear.feature_question.domain.use_case.*
import neet.code.flashwear.feature_settings.data.repository.SettingsRepositoryImpl
import neet.code.flashwear.feature_settings.domain.repository.SettingsRepository
import neet.code.flashwear.feature_settings.domain.use_case.ChangeSettings
import neet.code.flashwear.feature_settings.domain.use_case.GetSettings
import neet.code.flashwear.feature_settings.domain.use_case.SettingsUseCases
import neet.code.flashwear.feature_wearable.*
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
        )
            .fallbackToDestructiveMigration()
            .addCallback(CALLBACK)
            .build()
    }

    private val CALLBACK = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            db.execSQL("INSERT INTO Settings (id, learnStyle) VALUES (1, 'New')")
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
    fun provideDeckUseCases(learnSessionUseCases: LearnSessionUseCases, questionRepository: QuestionRepository, repository: DeckRepository, wearableUseCases: WearableUseCases): DecksUseCases {
        return DecksUseCases(
            getDecks = GetDecks(repository),
            addDeck = AddDeck(repository, wearableUseCases, learnSessionUseCases),
            getDecksForWearable = GetDecksForWearable(repository),
            deleteDeck = DeleteDeck(repository, questionRepository , wearableUseCases)
        )
    }

    @Provides
    @Singleton
    fun provideWearableUseCases(deckRepository: DeckRepository, @ApplicationContext context: Context): WearableUseCases {
        return WearableUseCases(
            syncDecks = SyncDecks(deckRepository, context),
            syncQuestions = SyncQuestions(context),
            syncQuestion = SyncQuestion(context),
            deleteQuestionOnWearable = DeleteQuestionOnWearable(context ),
            deleteDeckOnWearable = DeleteDeckOnWearable(context),
        )
    }

    @Provides
    @Singleton
    fun provideQuestionUseCases(wearableUseCases: WearableUseCases, repository: QuestionRepository, settingsRepository: SettingsRepository): QuestionsUseCases {
        return QuestionsUseCases(
            addQuestion = AddQuestion(repository, wearableUseCases),
            addQuestions = AddQuestions(repository, wearableUseCases),
            getQuestionsWithDeckFlow = GetQuestionsWithDeckFlow(repository),
            getQuestionsWithDeck = GetQuestionsWithDeck(repository, settingsRepository),
            deleteQuestion = DeleteQuestion(repository, wearableUseCases),
            updateQuestion = UpdateQuestion(repository, wearableUseCases),
            averageScoreOfDeck = AverageScoreOfDeck(repository),
            updateWearableScore = UpdateWearableScore(repository, wearableUseCases),
            getQuestionById = GetQuestionById(repository)
        )
    }

    @Provides
    @Singleton
    fun provideLearnSessionUseCases(repository: LearnSessionRepository, wearableUseCases: WearableUseCases, questionsUseCases: QuestionsUseCases): LearnSessionUseCases {
        return LearnSessionUseCases(
            startLearnSession = StartLearnSession(repository, wearableUseCases, questionsUseCases),
            updateLearnSession = UpdateLearnSession(repository, wearableUseCases, questionsUseCases),
            getAvgScoreByDeck = GetAvgScoreByDeck(repository),
            getMinutesLearnedByDeck = GetMinutesLearnedByDeck(repository),
            getAvgScoreQuestionsByDeck = GetAvgScoreQuestionsByDeck(repository),
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