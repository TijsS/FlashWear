package neet.code.flashwear.feature_learn_session.domain.use_case

import neet.code.flashwear.feature_question.domain.use_case.AddQuestion
import neet.code.flashwear.feature_question.domain.use_case.DeleteQuestion
import neet.code.flashwear.feature_question.domain.use_case.GetQuestionsWithDeck

data class LearnSessionUseCases (
    val startLearnSession: StartLearnSession,
    val updateLearnSession: UpdateLearnSession
)
