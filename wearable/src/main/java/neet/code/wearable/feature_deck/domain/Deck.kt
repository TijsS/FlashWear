package neet.code.wearable.feature_deck.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Deck(
    @PrimaryKey val id: Int ,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "created") val created: Long,
    @ColumnInfo(name = "timeStart") val timeStart: Long
)
