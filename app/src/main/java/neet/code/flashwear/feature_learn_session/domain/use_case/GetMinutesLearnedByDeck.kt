package neet.code.flashwear.feature_learn_session.domain.use_case

import android.util.Log
import com.madrapps.plot.line.DataPoint
import neet.code.flashwear.feature_deck.domain.util.dateToX
import neet.code.flashwear.feature_deck.domain.util.roundDouble
import neet.code.flashwear.feature_deck.presentation.view_deck.TimeScaleGraph
import neet.code.flashwear.feature_learn_session.domain.model.MinutesLearnedDTO
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository
import kotlin.math.log

class GetMinutesLearnedByDeck(
    private val repository: LearnSessionRepository
){
    /*
    * converts: 12-04-2022;timeSpend = to DataPoint(x(days since first learnsession);timespend)
    */
    suspend operator fun invoke(deckId: Long): MutableMap<TimeScaleGraph, List<DataPoint>> {
        val avgScoreQuestionsByDeck = repository.getMinutesLearnedByDeck(deckId)
        val avgScoreQuestionsDatapoints = mutableMapOf<TimeScaleGraph, List<DataPoint>>()

        for (timeScale in avgScoreQuestionsByDeck.keys) {
            if (!avgScoreQuestionsByDeck[timeScale].isNullOrEmpty()) {
                //get the first day this deck got used
                val dateOfFirstLearnSession = avgScoreQuestionsByDeck[timeScale]!!.first().getLocalDate(timeScale)
                val datapoints: MutableList<DataPoint> = mutableListOf()

                val xOfFinalLearnSession = dateToX(
                    dateOfFirstLearnSession = dateOfFirstLearnSession,
                    dateOfLearnSession = avgScoreQuestionsByDeck[timeScale]!!.last()
                        .getLocalDate(timeScale),
                    selectedTimeScale = timeScale
                ).toInt()

                //Fill all X with value of 0
                repeat(xOfFinalLearnSession + 1) {
                    datapoints.add(DataPoint(it.toFloat(), 0f))
                }

                //For every learnsession check how many X (days, weeks or months) it is removed from the first learnsession and fill with score
                for (learnSession in avgScoreQuestionsByDeck[timeScale]!!) {
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
                avgScoreQuestionsDatapoints[timeScale] = datapoints
            }
        }
        Log.i("TAG", "invoke!!!: $avgScoreQuestionsDatapoints")
        return avgScoreQuestionsDatapoints
    }
}