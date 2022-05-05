package neet.code.flashwear.feature_deck.domain.util

import neet.code.flashwear.feature_deck.presentation.view_deck.TimeScaleGraph
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.Months
import org.joda.time.Weeks

fun dateToX(dateOfFirstLearnSession: LocalDate?, dateOfLearnSession: LocalDate, selectedTimeScale: TimeScaleGraph): Float{
    return when(selectedTimeScale){
        TimeScaleGraph.Day -> {
            Days.daysBetween(
                dateOfFirstLearnSession,
                dateOfLearnSession
            ).days.toFloat()
        }
        TimeScaleGraph.Week -> {
            Weeks.weeksBetween(
                dateOfFirstLearnSession,
                dateOfLearnSession
            ).weeks.toFloat()
        }
        TimeScaleGraph.Month -> {
            Months.monthsBetween(
                dateOfFirstLearnSession,
                dateOfLearnSession
            ).months.toFloat()
        }
    }
}