package neet.code.wearable.feature_question.presentation

import neet.code.wearable.feature_deck.presentation.LearnStyle
import neet.code.wearable.feature_question.domain.Question


data class QuestionState(
        val allQuestions: List<Question> = emptyList(),

        var currentQuestion: Question? = null,
        val currentQuestionIndex: Int = -1,
        val deckId: Int = 0,
        val showAnswer: Boolean = false,
        val answeredQuestion: Boolean = false,
        val learnStyle: LearnStyle = LearnStyle.Revise,

        var finished: Boolean = false
    )