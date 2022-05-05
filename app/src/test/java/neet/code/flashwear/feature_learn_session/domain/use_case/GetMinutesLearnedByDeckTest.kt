package neet.code.flashwear.feature_learn_session.domain.use_case

import org.junit.Assert.*
import android.util.Log
import com.madrapps.plot.line.DataPoint
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import neet.code.flashwear.feature_deck.presentation.view_deck.TimeScaleGraph
import neet.code.flashwear.feature_learn_session.domain.model.AvgScoreDTO
import neet.code.flashwear.feature_learn_session.domain.model.MinutesLearnedDTO
import neet.code.flashwear.feature_learn_session.domain.repository.LearnSessionRepository
import org.joda.time.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate.now

@ExtendWith(MockKExtension::class)
class GetMinutesLearnedByDeckTest {

    // Subject under test
    private lateinit var getMinutesLearnedByDeck: GetMinutesLearnedByDeck

    var repository = mockk<LearnSessionRepository>()

    private val minutesLearnedDTODays: List<MinutesLearnedDTO> = listOf(
        MinutesLearnedDTO(minutesSpend = 50, timeStart = now().toEpochDay() * 86400000),
        MinutesLearnedDTO(minutesSpend = 99, timeStart = now().plusDays(2).toEpochDay() * 86400000 ),
    )
    private val minutesLearnedDTOWeeks: List<MinutesLearnedDTO> = listOf(
        MinutesLearnedDTO(minutesSpend = 50, timeStart = now().toEpochDay() * 86400000),
        MinutesLearnedDTO(minutesSpend = 99, timeStart = now().plusWeeks(2).toEpochDay() * 86400000 ),
    )
    private val minutesLearnedDTOMonths: List<MinutesLearnedDTO> = listOf(
        MinutesLearnedDTO(minutesSpend = 30, timeStart = now().toEpochDay() * 86400000),
        MinutesLearnedDTO(minutesSpend = 90, timeStart = now().plusMonths(2).toEpochDay() * 86400000 ),
    )

    @BeforeEach
    fun before(){
        //mock the logging
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        getMinutesLearnedByDeck = GetMinutesLearnedByDeck(repository = repository)
    }

    @Test
    fun `create avg score by deck`(){
        //given  1 day with score of 50% and 2 day later a day with a score of 99%
        every {
            runBlocking { repository.getMinutesLearnedByDeck(any()) }
        } returns mutableMapOf(
            Pair(TimeScaleGraph.Day, minutesLearnedDTODays),
            Pair(TimeScaleGraph.Week, minutesLearnedDTOWeeks),
            Pair(TimeScaleGraph.Month, minutesLearnedDTOMonths),
        )
        var minutesLearnedActual: MutableMap<TimeScaleGraph, List<DataPoint>>

        //when getAvgScoreByDeck is called
        runBlocking {
            minutesLearnedActual = getMinutesLearnedByDeck(1)
        }

        //then expect (for eacht timescale) the firstdata point to have a score of 50% then 0% and then 99%

        val minutesLearnedExpected = mutableMapOf(
            Pair(TimeScaleGraph.Day, listOf(
                DataPoint(0.0f, 50.0f),
                DataPoint(1.0f, 0.0f),
                DataPoint(2.0f, 99.0f),
            )),
            Pair(TimeScaleGraph.Week, listOf(
                DataPoint(0.0f, 50.0f),
                DataPoint(1.0f, 0.0f),
                DataPoint(2.0f, 99.0f),
            )),
            Pair(TimeScaleGraph.Month, listOf(
                DataPoint(0.0f, 0.5f),
                DataPoint(1.0f, 0.0f),
                DataPoint(2.0f, 1.5f),
            )),
        )

        assertEquals(minutesLearnedExpected, minutesLearnedActual)
    }
}