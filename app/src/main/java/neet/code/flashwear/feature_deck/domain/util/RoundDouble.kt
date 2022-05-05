package neet.code.flashwear.feature_deck.domain.util

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.roundDouble(): Double = BigDecimal(this).setScale(1, RoundingMode.HALF_EVEN).toDouble()
