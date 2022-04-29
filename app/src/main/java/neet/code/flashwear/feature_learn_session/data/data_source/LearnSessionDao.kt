package neet.code.flashwear.feature_learn_session.data.data_source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.core.data.data_source.BaseDao
import neet.code.flashwear.feature_learn_session.domain.model.AvgScoreDTO
import neet.code.flashwear.feature_learn_session.domain.model.MinutesLearnedDTO
import neet.code.flashwear.feature_learn_session.domain.model.LearnSession


@Dao
interface LearnSessionDao : BaseDao<LearnSession> {

    @Transaction
    @Query("SELECT * FROM learnSession")
    fun getAll(): Flow<List<LearnSession>>


    @Transaction
    @Query("SELECT * FROM learnSession where learnSession.id = :id")
    fun getById(id: Long): LearnSession

    @Transaction
    @Query("""select round(avg(score),1) as score, timeStart
        from learnsession 
        where deckId = :id
        group by strftime(:timeScale, timeStart/ 1000, 'unixepoch')""")
    suspend fun getAvgScoreByDeck(id: Long, timeScale: String): List<AvgScoreDTO>

    @Transaction
    @Query("""select round(avg(questionsScore) * 100,1) as score, timeStart
        from learnsession 
        where deckId = :id
        group by strftime(:timeScale, timeStart/ 1000, 'unixepoch')""")
    suspend fun getAvgScoreQuestionsByDeck(id: Long, timeScale: String): List<AvgScoreDTO>

    @Transaction
    @Query("""select round(cast((sum(timeEnd-timeStart)) as REAL)  / 1000/60, 3) as minutesSpend, timeStart
        from learnsession 
        where deckId = :id
        group by strftime(:timeScale, timeStart/ 1000, 'unixepoch')""")
    suspend fun getMinutesLearnedByDeck(id: Long, timeScale: String): List<MinutesLearnedDTO>

}