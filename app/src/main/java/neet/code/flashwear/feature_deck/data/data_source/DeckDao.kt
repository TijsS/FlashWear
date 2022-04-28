package neet.code.flashwear.feature_deck.data.data_source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import neet.code.flashwear.core.data.data_source.BaseDao
import kotlinx.coroutines.flow.Flow
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.model.WearableDeckDTO

@Dao
interface DeckDao : BaseDao<Deck> {

    @Transaction
    @Query("""select id, name, created, ls.timeStart 
            from deck 
            join (  select  deckId, max(timeStart), timeStart
                    from learnsession 
                    group by deckId ) ls
            on ls.deckId = deck.id 
            union all 
            select deck.id, deck.name, deck.created, ls.timestart 
            from deck 
            left join learnsession ls 
            on ls.deckId = deck.id
            where ls.deckId is null 
            order by ls.timeStart""")
    fun getDecksByLastPlayedFlow(): Flow<List<Deck>>

    @Transaction
    @Query(""" select id, name, created, ls.timeStart 
            from deck 
            join (  select deckId, max(timeStart), timeStart
                    from learnsession 
                    group by deckId ) ls 
            on ls.deckId = deck.id 
            union all 
            select deck.id, deck.name, deck.created, ls.timestart 
            from deck 
            left join learnsession ls 
            on ls.deckId = deck.id 
            where ls.deckId is null 
            order by ls.timeStart """)
    suspend fun getDecksForWearableByLastPlayed(): List<WearableDeckDTO>

    @Transaction
    @Query("SELECT * FROM deck where deck.id = :id")
    suspend fun getDeckById(id: Int): Deck?

    @Transaction
    @Query("DELETE FROM deck where deck.id = :id")
    suspend fun deleteById(id: Int)
}