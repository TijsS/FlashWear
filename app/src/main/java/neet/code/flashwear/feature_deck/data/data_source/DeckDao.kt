package neet.code.flashwear.feature_deck.data.data_source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import neet.code.flashwear.core.data.data_source.BaseDao
import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_deck.domain.model.Deck

@Dao
interface DeckDao : BaseDao<Deck> {

    @Transaction
    @Query("SELECT * FROM deck")
    fun getAll(): Flow<List<Deck>>

    @Transaction
    @Query("SELECT * FROM deck where deck.id = :id")
    suspend fun getDeckById(id: Int): Deck?
}