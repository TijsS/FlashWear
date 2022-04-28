package neet.code.flashwear.feature_deck.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class WearableDeckDTO(
    val id: Int,
    val name: String,
    val created: Long,
    val timeStart: Long,
    )

