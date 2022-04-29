package neet.code.flashwear.feature_learn_session.domain.model

import neet.code.flashwear.feature_deck.presentation.view_deck.TimeScaleGraph
import org.joda.time.LocalDate

data class AvgScoreDTO(
//    val id: Int,
    val score: Double,
    val timeStart: Long
    ){

    fun getLocalDate(selectedTimeScale: TimeScaleGraph): LocalDate {
        var date = LocalDate(this.timeStart)

        when(selectedTimeScale){
            TimeScaleGraph.Day -> {
            }
            TimeScaleGraph.Week -> {
                date = date.withDayOfWeek(1)
            }
            TimeScaleGraph.Month -> {
                date = date.withDayOfMonth(1)
            }
        }
        return date
    }
}