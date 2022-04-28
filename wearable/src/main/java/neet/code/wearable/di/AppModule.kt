package neet.code.wearable.di

import android.app.Application
import android.content.Context
import androidx.room.Room

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import neet.code.wearable.data.FlashWearDatabase
import neet.code.wearable.feature_deck.domain.repository.DeckRepository
import neet.code.wearable.feature_deck.domain.use_case.DecksUseCases
import neet.code.wearable.feature_deck.domain.use_case.GetDecks
import neet.code.wearable.feature_deck.data.DeckRepositoryImpl
import neet.code.wearable.feature_deck.domain.use_case.DeleteDeck
import neet.code.wearable.feature_deck.domain.use_case.SyncDecks
import neet.code.wearable.feature_question.data.QuestionRepositoryImpl
import neet.code.wearable.feature_question.domain.repository.QuestionRepository
import neet.code.wearable.feature_question.domain.use_case.*
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
            FlashWearDatabase.DATABASE_NAME)
            .build()
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


    //UseCases
    @Provides
    @Singleton
    fun provideDeckUseCases(repository: DeckRepository, questionRepository: QuestionRepository): DecksUseCases {
        return DecksUseCases(
            getDecks = GetDecks(repository),
            syncDecks = SyncDecks(repository, questionRepository),
            deleteDeck = DeleteDeck(repository, questionRepository),
        )
    }

    @Provides
    @Singleton
    fun provideQuestionUseCases(repository: QuestionRepository, @ApplicationContext context: Context): QuestionsUseCases {
        return QuestionsUseCases(
            sendQuestionResult = SendQuestionResult(context),
            syncQuestions = SyncQuestions(repository),
            getQuestionsWithDeck = GetQuestionsWithDeck(repository),
            syncQuestion = SyncQuestion(repository),
            deleteQuestion = DeleteQuestion(repository),
        )
    }
}