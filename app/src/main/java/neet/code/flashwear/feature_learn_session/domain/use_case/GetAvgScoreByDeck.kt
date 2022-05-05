package neet.code.flashwear.feature_learn_session.domain.use_case

import android.util.Log
import com.madrapps.plot.line.DataPoint
import neet.code.flashwear.feature_deck.domain.util.dateToX
import neet.code.flashwear.feature_deck.domain.util.roundDouble
import neet.code.flashwear.feature_deck.presentation.view_deck.TimeScaleGraph
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository
import org.joda.time.LocalDate

class GetAvgScoreByDeck(
    private val repository: LearnSessionRepository
    ) {
    /*
    * converts: 12-04-2022;AvgScore to DataPoint(x(days since first learnsession);AvgScore)
    */
    suspend operator fun invoke(deckId: Long): Pair<MutableMap<TimeScaleGraph, List<DataPoint>>, LocalDate> {
        val avgScoreByDeck = repository.getAvgScoreByDeck(deckId)
        val avgScoreDatapoints = mutableMapOf<TimeScaleGraph, List<DataPoint>>()

        val firstDay = avgScoreByDeck[TimeScaleGraph.Day]!!.first().getLocalDate(TimeScaleGraph.Day)

        for(timeScale in avgScoreByDeck.keys){
            if(!avgScoreByDeck[timeScale].isNullOrEmpty()){
                    //get the first day this deck got used
                val dateOfFirstLearnSession = avgScoreByDeck[timeScale]!!.first().getLocalDate(timeScale)
                val datapoints: MutableList<DataPoint> = mutableListOf()

                val xOfFinalLearnSession = dateToX(
                        dateOfFirstLearnSession = dateOfFirstLearnSession,
                        dateOfLearnSession = avgScoreByDeck[timeScale]!!.last().getLocalDate(timeScale),
                        selectedTimeScale = timeScale
                    ).toInt()

                //Fill all X with value of 0
                repeat(xOfFinalLearnSession + 1){
                    datapoints.add(DataPoint(it.toFloat(), 0f))
                }

                //For every learnsession check how many X (days, weeks or months) it is removed from the first learnsession and fill with score
                for (learnSession in avgScoreByDeck[timeScale]!!) {
                    val xAxis = dateToX(
                            dateOfFirstLearnSession = dateOfFirstLearnSession,
                            dateOfLearnSession = learnSession.getLocalDate(timeScale),
                            selectedTimeScale = timeScale
                        )
                    datapoints[xAxis.toInt()] = DataPoint(
                            xAxis,
                            learnSession.score.roundDouble().toFloat()
                        )
                }
                avgScoreDatapoints[timeScale] = datapoints
            }
        }
        return Pair(avgScoreDatapoints, firstDay)
    }
}
