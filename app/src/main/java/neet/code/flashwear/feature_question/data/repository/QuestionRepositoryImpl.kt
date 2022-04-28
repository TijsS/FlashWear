package neet.code.flashwear.feature_question.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_question.data.data_source.QuestionDao
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.repository.QuestionRepository

class QuestionRepositoryImpl(
    private val dao: QuestionDao
    ) : QuestionRepository {


    override fun getQuestionsByDeckFlow(deckId: Int): Flow<List<Question>> {
        return dao.getQuestionsByDeckIdFlow(deckId)
    }

    override suspend fun getQuestionsByDeck(deckId: Int): List<Question> {
        return dao.getQuestionsByDeckId(deckId)
    }

    override suspend fun getOldQuestionsByDeck(deckId: Int): List<Question> {
        return dao.getOldQuestionsByDeck(deckId)
    }

    override suspend fun getNewQuestionsByDeck(deckId: Int): List<Question> {
        return dao.getNewQuestionsByDeck(deckId)
    }

    override suspend fun getQuestionById(id: Int): Question {
        return dao.getQuestionById(id)
    }

    override suspend fun getQuestionsFromIdList(questionIds: List<Long>): List<Question> {
        return dao.getQuestionsFromIdList(questionIds)
    }

    override suspend fun insertQuestion(question: Question) {
        dao.insert(question)
    }

    override suspend fun insertQuestions(question: List<Question>): List<Long> {
        return dao.insertBatch(question)
    }

    override suspend fun deleteQuestion(question: Question) {
        return dao.delete(question)
    }

    override suspend fun deleteQuestionById(questionId: Int) {
        return dao.deleteQuestionById(questionId)
    }

    override suspend fun deleteQuestionsWithDeck(deckId: Int) {
        return dao.deleteQuestionsWithDeck(deckId)
    }

    override suspend fun updateQuestion(question: Question) {
        return dao.update(question)
    }

    override suspend fun averageScoreOfDeck(deckId: Int): Float {
        val x = dao.averageScoreOfDeck(deckId)
        return x
    }
}