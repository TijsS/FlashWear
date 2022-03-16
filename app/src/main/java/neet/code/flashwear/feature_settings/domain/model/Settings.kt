package neet.code.flashwear.feature_settings.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var learnStyle: LearnStyle = LearnStyle.NewWords
)

enum class LearnStyle{
    NewWords, Revise, Random
}

