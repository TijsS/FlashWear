package neet.code.flashwear.feature_learn_session.domain.use_case

data class LearnSessionUseCases (
    val startLearnSession: StartLearnSession,
    val getAvgScoreByDeck: GetAvgScoreByDeck,
    val updateLearnSession: UpdateLearnSession,
    val getMinutesLearnedByDeck: GetMinutesLearnedByDeck,
    val getAvgScoreQuestionsByDeck: GetAvgScoreQuestionsByDeck,
)
