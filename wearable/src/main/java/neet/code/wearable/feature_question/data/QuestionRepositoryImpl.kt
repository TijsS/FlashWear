package neet.code.wearable.feature_question.data

import kotlinx.coroutines.flow.Flow
import neet.code.wearable.feature_question.domain.Question
import neet.code.wearable.feature_question.domain.repository.QuestionRepository


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

    override suspend fun insertQuestion(question: Question) {
        return dao.insert(question)
    }

    override suspend fun insertQuestionOnConflictIgnore(question: Question): Long {
        return dao.insertOnConflictIgnore(question)
    }

    override suspend fun insertQuestions(question: List<Question>) {
        return dao.insertBatch(question)
    }

    override suspend fun deleteQuestionById(questionId: Int) {
        return dao.deleteById(questionId)
    }

    override suspend fun deleteQuestionsWithDeck(deckId: Int) {
        return dao.deleteQuestionsWithDeck(deckId)
    }

    override suspend fun updateQuestion(question: Question): Int {
        return dao.update(question)
    }

    override suspend fun updateQuestionBatch(question: List<Question>) {
        return dao.updateQuestionBatch(question)
    }
}