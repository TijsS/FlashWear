package neet.code.flashwear.feature_question.data.data_source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.core.data.data_source.BaseDao
import neet.code.flashwear.feature_question.domain.model.Question


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
    @Query("select IFNULL(avg(q.score), 0.0) from question q where q.deckId = :id")
    suspend fun averageScoreOfDeck(id: Int): Float
}