package neet.code.flashwear.feature_learn_session.domain.use_case

import android.util.Log
import com.madrapps.plot.line.DataPoint
import neet.code.flashwear.feature_deck.domain.util.dateToX
import neet.code.flashwear.feature_deck.domain.util.roundDouble
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository
import neet.code.flashwear.feature_progress.presentation.progress.TimeScaleGraph
import org.joda.time.LocalDate

class GetTimeSpentTotal(
    private val repository: LearnSessionRepository
){
    /*
    * converts: 12-04-2022;timeSpend = to DataPoint(x(days since first learnsession);timespend)
    */
    suspend operator fun invoke(): Pair<MutableMap<TimeScaleGraph, List<DataPoint>>, LocalDate> {
        val minutesSpentTotal = repository.getMinutesLearnedTotal()
        Log.i("TAG","${minutesSpentTotal}")

        val minutesSpentTotalDatapoints = mutableMapOf<TimeScaleGraph, List<DataPoint>>()

        val firstDay = minutesSpentTotal[TimeScaleGraph.Day]!!.first().getLocalDate(TimeScaleGraph.Day)

        for (timeScale in minutesSpentTotal.keys) {
            if (!minutesSpentTotal[timeScale].isNullOrEmpty()) {
                //get the first day this deck got used
                val dateOfFirstLearnSession = minutesSpentTotal[timeScale]!!.first().getLocalDate(timeScale)
                val datapoints: MutableList<DataPoint> = mutableListOf()

                val xOfFinalLearnSession = dateToX(
                    dateOfFirstLearnSession = dateOfFirstLearnSession,
                    dateOfLearnSession = minutesSpentTotal[timeScale]!!.last()
                        .getLocalDate(timeScale),
                    selectedTimeScale = timeScale
                ).toInt()

                //Fill all X with value of 0
                repeat(xOfFinalLearnSession + 1) {
                    datapoints.add(DataPoint(it.toFloat(), 0f))
                }

                //For every learnsession check how many X (days, weeks or months) it is removed from the first learnsession and fill with score
                for (learnSession in minutesSpentTotal[timeScale]!!) {
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
                minutesSpentTotalDatapoints[timeScale] = datapoints
            }
        }
        return Pair(minutesSpentTotalDatapoints, firstDay)
    }
}