package neet.code.flashwear.feature_question.presentation.add_question

import neet.code.flashwear.feature_question.domain.model.Question

data class AddQuestionState (

    val questionTitle: String = "",
    val questionContent: String = "",
    val answerTitle: String = "",
    val answerContent: String = "",
    val answerSub: String = "",
    val deckId: Int = 0
)