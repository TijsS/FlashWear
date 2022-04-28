package neet.code.flashwear.feature_learn_session.presentation.start_learn_session

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import neet.code.flashwear.feature_learn_session.domain.model.LearnSession
import neet.code.flashwear.feature_question.domain.model.Question

data class StartLearnSessionState(
    var allQuestionsBatch: List<List<Question>> = emptyList(),
    val currentQuestionsBatch: List<Question> = mutableListOf(),
    var currentBatchIndex: Int = 0,

    var currentQuestion: Question? = null,
    val currentQuestionIndex: Int = 0,
    val showAnswer: Boolean = false,
    val answeredQuestion: Boolean = false,
    val learnSession: LearnSession? = null,
    val deckName: String = "",
    var finished: Boolean = false
)