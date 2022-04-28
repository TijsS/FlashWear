package neet.code.wearable.feature_deck.presentation

import neet.code.wearable.feature_deck.domain.Deck

data class DecksState (
    val decks: List<Deck> = emptyList(),
    val learnStyle: LearnStyle = LearnStyle.Revise
)

enum class LearnStyle{
    New, Revise, Random
}