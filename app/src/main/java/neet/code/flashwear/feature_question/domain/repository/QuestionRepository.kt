package neet.code.flashwear.feature_question.domain.repository

import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_question.domain.model.Question

interface QuestionRepository {
    fun getQuestionsByDeckFlow(deckId: Int): Flow<List<Question>>

    suspend fun getQuestionsByDeck(deckId: Int): List<Question>

    suspend fun getNewQuestionsByDeck(deckId: Int): List<Question>

    suspend fun getOldQuestionsByDeck(deckId: Int): List<Question>

    suspend fun getQuestionsFromIdList(questionIds: List<Long>): List<Question>

    suspend fun getQuestionById(id: Int): Question?

    suspend fun insertQuestion(question: Question)

    suspend fun insertQuestions(question: List<Question>): List<Long>

    suspend fun deleteQuestion(question: Question)

    suspend fun deleteQuestionById(questionId: Int)

    suspend fun deleteQuestionsWithDeck(deckId: Int)

    suspend fun updateQuestion(question: Question)

    suspend fun averageScoreOfDeck(deckId: Int): Float

}