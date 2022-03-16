package neet.code.flashwear.feature_deck.domain.util

sealed class DeckOrder(val orderType: OrderType) {
    class Name(orderType: OrderType): DeckOrder(orderType)
    class Date(orderType: OrderType): DeckOrder(orderType)
    class LastPlayed(orderType: OrderType): DeckOrder(orderType)

    fun copy(orderType: OrderType): DeckOrder{
        return when(this) {
            is Name -> Name(orderType)
            is Date -> Date(orderType)
            is LastPlayed -> LastPlayed(orderType)
        }
    }
}