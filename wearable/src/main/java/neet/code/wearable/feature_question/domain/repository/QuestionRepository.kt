package neet.code.wearable.feature_question.domain.repository

import kotlinx.coroutines.flow.Flow
import neet.code.wearable.feature_question.domain.Question

interface QuestionRepository {
    fun getQuestionsByDeckFlow(deckId: Int): Flow<List<Question>>

    suspend fun getQuestionsByDeck(deckId: Int): List<Question>

    suspend fun getNewQuestionsByDeck(deckId: Int): List<Question>

    suspend fun getOldQuestionsByDeck(deckId: Int): List<Question>

    suspend fun getQuestionById(id: Int): Question?

    suspend fun insertQuestion(question: Question)

    suspend fun insertQuestionOnConflictIgnore(question: Question): Long

    suspend fun insertQuestions(question: List<Question>)

    suspend fun deleteQuestionById(questionId: Int)

    suspend fun deleteQuestionsWithDeck(deckId: Int)

    suspend fun updateQuestion(question: Question): Int

    suspend fun updateQuestionBatch(question: List<Question>)
}