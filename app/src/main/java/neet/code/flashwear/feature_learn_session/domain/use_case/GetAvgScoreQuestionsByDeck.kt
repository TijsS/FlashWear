package neet.code.flashwear.feature_learn_session.domain.use_case

import com.madrapps.plot.line.DataPoint
import neet.code.flashwear.feature_deck.domain.util.dateToX
import neet.code.flashwear.feature_deck.domain.util.roundDouble
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository
import neet.code.flashwear.feature_progress.presentation.progress.TimeScaleGraph


class GetAvgScoreQuestionsByDeck(
    private val repository: LearnSessionRepository
) {
    /*
    * converts: 12-04-2022;AvgScoreOfQuestions = to DataPoint(x(days since first learnsession);AvgScoreOfQuestions)
    */
    suspend operator fun invoke(deckId: Long): MutableMap<TimeScaleGraph, List<DataPoint>> {
        val avgScoreQuestionsByDeck = repository.getAvgScoreQuestionsByDeck(deckId)
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
                        learnSession.score.roundDouble().toFloat()
                    )
                }
                avgScoreQuestionsDatapoints[timeScale] = datapoints
            }
        }
        return avgScoreQuestionsDatapoints
    }
}
