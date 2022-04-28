package neet.code.flashwear.feature_learn_session.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LearnSession(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    //percentage of correctly answered questions (including repeating questions)
    @ColumnInfo(name = "score") var score : Double? = null,
    //score of all questions (also unanswered)
    @ColumnInfo(name = "questionsScore") var questionsScore : Double? = null,
    @ColumnInfo(name = "timeStart") val timeStart : Long = System.currentTimeMillis(),
    @ColumnInfo(name = "timeEnd") var timeEnd : Long? = null,
    @ColumnInfo(name = "deckId") val deckId : Int,
    )