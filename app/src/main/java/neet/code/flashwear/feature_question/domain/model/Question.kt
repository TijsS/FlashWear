package neet.code.flashwear.feature_question.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity
data class Question(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "questionTitle") var questionTitle: String? = null,
    @ColumnInfo(name = "questionContent") var questionContent: String? = null,
    @ColumnInfo(name = "answerTitle") var answerTitle: String? = null,
    @ColumnInfo(name = "answerContent") var answerContent: String? = null,
    @ColumnInfo(name = "answerSub") var answerSub: String? = null,
    @ColumnInfo(name = "score") var score : Double = 0.1,
    @ColumnInfo(name = "deckId") val deckId : Int? = -1,
    ){
    fun toCsvRow(): List<String>{
        return listOf("${if(questionTitle != null) questionTitle else ""}", "${if(questionContent != null) questionContent else ""}","${if(answerTitle != null) answerTitle else ""}","${if(answerContent != null) answerContent else ""}","${if(answerSub != null) answerSub else ""}")
    }
}

class InvalidQuestionException(message: String = "", baseMessage: Int): Exception("$message|$baseMessage")