package neet.code.wearable.feature_deck.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import neet.code.wearable.core.data.data_source.BaseDao
import kotlinx.coroutines.flow.Flow
import neet.code.wearable.feature_deck.domain.Deck


@Dao
interface DeckDao : BaseDao<Deck> {

    @Transaction
    @Query("SELECT * FROM deck order by deck.timeStart desc")
    fun getAll(): Flow<List<Deck>>

    @Transaction
    @Query("SELECT * FROM deck")
    suspend fun getDecksForWearable(): List<Deck>

    @Transaction
    @Query("SELECT * FROM deck where deck.id = :id")
    suspend fun getDeckById(id: Int): Deck?

    @Transaction
    @Query("DELETE FROM deck where deck.id = :id")
    suspend fun deleteDeckById(id: Int)

    @Transaction
    @Query("select id from Deck")
    suspend fun getAllDeckIds(): MutableList<Int>
}