package neet.code.flashwear.feature_learn_session.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.core.data.data_source.BaseDao
import neet.code.flashwear.feature_learn_session.domain.model.LearnSession
import neet.code.flashwear.feature_question.domain.model.Question


@Dao
interface LearnSessionDao : BaseDao<LearnSession> {

    @Transaction
    @Query("SELECT * FROM learnSession")
    fun getAll(): Flow<List<LearnSession>>


    @Transaction
    @Query("SELECT * FROM learnSession where learnSession.id = :id")
    fun getById(id: Long): LearnSession

    @Insert
    suspend fun insertLearnSession(learnSession: LearnSession): Long
}