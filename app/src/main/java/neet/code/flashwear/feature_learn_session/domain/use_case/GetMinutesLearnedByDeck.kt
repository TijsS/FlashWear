package neet.code.flashwear.feature_learn_session.domain.use_case

import android.util.Log
import com.madrapps.plot.line.DataPoint
import neet.code.flashwear.feature_deck.domain.util.dateToX
import neet.code.flashwear.feature_deck.domain.util.roundDouble
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository
import neet.code.flashwear.feature_progress.presentation.progress.TimeScaleGraph

class GetMinutesLearnedByDeck(
    private val repository: LearnSessionRepository
){
    /*
    * converts: 12-04-2022;timeSpend = to DataPoint(x(days since first learnsession);timespend)
    */
    suspend operator fun invoke(deckId: Long): MutableMap<TimeScaleGraph, List<DataPoint>> {
        val minutesLearned = repository.getMinutesLearnedByDeck(deckId)
        val minutesLearnedDatapoints = mutableMapOf<TimeScaleGraph, List<DataPoint>>()

        for (timeScale in minutesLearned.keys) {
            if (!minutesLearned[timeScale].isNullOrEmpty()) {
                //get the first day this deck got used
                val dateOfFirstLearnSession = minutesLearned[timeScale]!!.first().getLocalDate(timeScale)
                val datapoints: MutableList<DataPoint> = mutableListOf()

                val xOfFinalLearnSession = dateToX(
                    dateOfFirstLearnSession = dateOfFirstLearnSession,
                    dateOfLearnSession = minutesLearned[timeScale]!!.last()
                        .getLocalDate(timeScale),
                    selectedTimeScale = timeScale
                ).toInt()

                //Fill all X with value of 0
                repeat(xOfFinalLearnSession + 1) {
                    datapoints.add(DataPoint(it.toFloat(), 0f))
                }

                //For every learnsession check how many X (days, weeks or months) it is removed from the first learnsession and fill with score
                for (learnSession in minutesLearned[timeScale]!!) {
                    val xAxis = dateToX(
                        dateOfFirstLearnSession = dateOfFirstLearnSession,
                        dateOfLearnSession = learnSession.getLocalDate(timeScale),
                        selectedTimeScale = timeScale
                    )

                    datapoints[xAxis.toInt()] = DataPoint(
                        xAxis,
                        learnSession.minutesSpend.div(if (timeScale == TimeScaleGraph.Month) 60.0 else 1.0)
                            .roundDouble()
                            .toFloat()
                    )
                }
                minutesLearnedDatapoints[timeScale] = datapoints
            }
        }
        Log.i("TAG", "invoke!!!: $minutesLearnedDatapoints")
        return minutesLearnedDatapoints
    }
}