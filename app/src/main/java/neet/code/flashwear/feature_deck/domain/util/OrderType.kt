package neet.code.flashwear.feature_deck.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}