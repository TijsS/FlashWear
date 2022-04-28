package neet.code.wearable.feature_question.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import neet.code.wearable.core.data.data_source.BaseDao
import neet.code.wearable.feature_question.domain.Question

@Dao
interface QuestionDao : BaseDao<Question> {

    @Transaction
    @Query("SELECT * FROM question")
    fun getAll(): Flow<List<Question>>

    @Transaction
    @Query("SELECT * FROM question where question.deckId = :id")
    suspend fun getQuestionsByDeckId(id: Int): List<Question>

    @Transaction
    @Query("SELECT * FROM question where question.deckId = :id and question.score > 0.1")
    suspend fun getOldQuestionsByDeck(id: Int): List<Question>

    @Transaction
    @Query("SELECT * FROM question where question.deckId = :id and question.score = 0.1")
    suspend fun getNewQuestionsByDeck(id: Int): List<Question>

    @Transaction
    @Query("SELECT * FROM question where question.deckId = :id")
    fun getQuestionsByDeckIdFlow(id: Int): Flow<List<Question>>

    @Transaction
    @Query("SELECT * FROM question where question.id = :id")
    suspend fun getQuestionById(id: Int): Question

    @Transaction
    @Query("DELETE FROM question where question.id = :id")
    suspend fun deleteById(id: Int)

    @Transaction
    @Query("DELETE FROM question where question.deckId = :id")
    suspend fun deleteQuestionsWithDeck(id: Int)

    @Transaction
    @Query("select IFNULL(avg(question.score), 0.0) from question where question.deckId = :id")
    suspend fun averageScoreOfDeck(id: Int): Float

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateQuestionBatch (question: List<Question>)
}