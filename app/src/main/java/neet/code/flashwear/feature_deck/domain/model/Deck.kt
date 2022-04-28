package neet.code.flashwear.feature_deck.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Deck(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "created") val created: Long = System.currentTimeMillis(),
    )

class InvalidDeckException(message: String): Exception(message)