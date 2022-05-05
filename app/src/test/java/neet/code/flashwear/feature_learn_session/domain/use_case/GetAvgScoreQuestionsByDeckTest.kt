package neet.code.flashwear.feature_learn_session.domain.use_case

import android.util.Log
import com.madrapps.plot.line.DataPoint
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import neet.code.flashwear.feature_deck.presentation.view_deck.TimeScaleGraph
import neet.code.flashwear.feature_learn_session.domain.model.AvgScoreDTO
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository
import org.junit.Assert.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate.now


@ExtendWith(MockKExtension::class)
class GetAvgScoreQuestionsByDeckTest {

    // Subject under test
    private lateinit var getAvgScoreQuestionsByDeck: GetAvgScoreQuestionsByDeck

    var repository = mockk<LearnSessionRepository>()

    private val avgScoreDTODays: List<AvgScoreDTO> = listOf(
        AvgScoreDTO(score = 50.0, timeStart = now().toEpochDay() * 86400000),
        AvgScoreDTO(score = 99.0, timeStart = now()
            .plusDays(2).toEpochDay() * 86400000 ),
    )
    private val avgScoreDTOWeeks: List<AvgScoreDTO> = listOf(
        AvgScoreDTO(score = 50.0, timeStart = now().toEpochDay() * 86400000),
        AvgScoreDTO(score = 99.0, timeStart = now()
            .plusWeeks(2).toEpochDay() * 86400000 ),
    )
    private val avgScoreDTOMonths: List<AvgScoreDTO> = listOf(
        AvgScoreDTO(score = 50.0, timeStart = now().toEpochDay() * 86400000),
        AvgScoreDTO(score = 99.0, timeStart = now()
            .plusMonths(2).toEpochDay() * 86400000 ),
    )

    @BeforeEach
    fun before(){
        //mock the logging
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        getAvgScoreQuestionsByDeck = GetAvgScoreQuestionsByDeck(repository = repository)
    }

    @Test
    fun `create avg score by deck`(){
        //given  1 day with score of 50% and 2 day later a day with a score of 99%
        every {
            runBlocking { repository.getAvgScoreQuestionsByDeck(any()) }
        } returns mutableMapOf(
            Pair(TimeScaleGraph.Day, avgScoreDTODays),
            Pair(TimeScaleGraph.Week, avgScoreDTOWeeks),
            Pair(TimeScaleGraph.Month, avgScoreDTOMonths),
        )
        var avgScoreQuestionsActual: MutableMap<TimeScaleGraph, List<DataPoint>>

        //when getAvgScoreByDeck is called
        runBlocking {
            avgScoreQuestionsActual = getAvgScoreQuestionsByDeck(1)
        }
/*
        then expect (for each timescale) the first data point to have a score of 50 minutes, and then 90 minutes
        for timescale month expect 0.5 hours, 0 hours, 1.5 hours
*/

        val avgScoreQuestionsExpected = mutableMapOf(
            Pair(
                TimeScaleGraph.Day, listOf(
                DataPoint(0.0f, 50.0f),
                DataPoint(1.0f, 0.0f),
                DataPoint(2.0f, 99.0f),
            )),
            Pair(
                TimeScaleGraph.Week, listOf(
                DataPoint(0.0f, 50.0f),
                DataPoint(1.0f, 0.0f),
                DataPoint(2.0f, 99.0f),
            )),
            Pair(
                TimeScaleGraph.Month, listOf(
                DataPoint(0.0f, 0.5f),
                DataPoint(1.0f, 0.0f),
                DataPoint(2.0f, 1.5f),
            )),
        )

        assertEquals(avgScoreQuestionsExpected, avgScoreQuestionsActual)
    }
}