package neet.code.flashwear.feature_question.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "questionTitle") var questionTitle: String? = null,
    @ColumnInfo(name = "questionContent") var questionContent: String? = null,
    @ColumnInfo(name = "answerTitle") var answerTitle: String? = null,
    @ColumnInfo(name = "answerContent") var answerContent: String? = null,
    @ColumnInfo(name = "answerSub") var answerSub: String? = null,
    @ColumnInfo(name = "score") var score : Double? = 0.1,
    @ColumnInfo(name = "deckId") val deckId : Int? = -1,
    )

class InvalidQuestionException(message: String): Exception(message)