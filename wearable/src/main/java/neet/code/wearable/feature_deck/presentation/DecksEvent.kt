package neet.code.wearable.feature_deck.presentation

sealed class DecksEvent {
    data class ChangeLearnStyle(val learnStyle: LearnStyle): DecksEvent()

}