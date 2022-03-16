package neet.code.flashwear.feature_deck.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Deck(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "questions") val questions: String,
    @ColumnInfo(name = "statistics") val statistics: String,
    @ColumnInfo(name = "created") val created: Long,
    @ColumnInfo(name = "last_played") val lastPlayed: Long,
    )

class InvalidDeckException(message: String): Exception(message)