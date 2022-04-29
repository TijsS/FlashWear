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
    @Query("SELECT * FROM question where question.deckId = :id order by score DESC")
    fun getQuestionsByDeckIdFlow(id: Int): Flow<List<Question>>

    @Transaction
    @Query("SELECT * FROM question where question.id = :id")
    suspend fun getQuestionById(id: Int): Question

    @Transaction
    @Query("SELECT * FROM question where question.id in (:ids)")
    suspend fun getQuestionsFromIdList(ids: List<Long>): List<Question>

    @Transaction
    @Query("DELETE FROM question where question.id = :id")
    suspend fun deleteQuestionById(id: Int)

    @Transaction
    @Query("DELETE FROM question where question.deckId = :id")
    suspend fun deleteQuestionsWithDeck(id: Int)

    @Transaction
    @Query("select IFNULL(avg(question.score), 0.0) from question where question.deckId = :id")
    suspend fun averageScoreOfDeck(id: Int): Float
}