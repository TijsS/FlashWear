package neet.code.flashwear.feature_learn_session.presentation.start_learn_session


sealed class StartLearnSessionEvent {
    data class NextQuestion(val value: String): StartLearnSessionEvent()

    object AddLearnSession: StartLearnSessionEvent()
    object EnteredIfCorrect: StartLearnSessionEvent()

    object ShowAnswer: StartLearnSessionEvent()
}