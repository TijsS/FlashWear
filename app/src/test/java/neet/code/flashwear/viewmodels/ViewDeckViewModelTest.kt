package neet.code.flashwear.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.madrapps.plot.line.DataPoint
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import neet.code.flashwear.feature_deck.domain.use_case.DecksUseCases
import neet.code.flashwear.feature_learn_session.domain.model.AvgScoreDTO
import neet.code.flashwear.feature_learn_session.domain.model.MinutesLearnedDTO
import neet.code.flashwear.feature_learn_session.domain.use_case.LearnSessionUseCases
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import neet.code.flashwear.feature_wearable.WearableUseCases
import neet.code.flashwear.feature_deck.presentation.view_deck.ProgressGraph
import neet.code.flashwear.feature_deck.presentation.view_deck.TimeScaleGraph
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckViewModel
import org.joda.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

//import org.mockito.Mockito.mock

@ExtendWith(MockKExtension::class)
class ViewDeckViewModelTest {

    // Subject under test
    private lateinit var viewDeckViewModel: ViewDeckViewModel

    @BeforeEach
    fun before(){
        //mock the logging
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        val decksUseCases = mockk<DecksUseCases>()
        val learnSessionUseCases = mockk<LearnSessionUseCases>()
        val questionsUseCases = mockk<QuestionsUseCases>()
        val wearableUseCases = mockk<WearableUseCases>()

        val savedStateHandle = SavedStateHandle().apply {
            set("deckId", 1)
            set("deckName", "deck1")
        }

        viewDeckViewModel = ViewDeckViewModel(
            savedStateHandle,
            decksUseCases = decksUseCases,
            learnSessionUseCases = learnSessionUseCases,
            questionsUseCases = questionsUseCases,
            wearableUseCases = wearableUseCases
        )
    }

    @ParameterizedTest(name = "{arguments}")
    @CsvSource(
        "5,     17-01-22",
        "-5,    07-01-22",
    )
    fun `getDateForX get date with Day`(x: Int, expectedDate: String) {
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Day

        //given Date of the first learnsession
        viewDeckViewModel.viewDeckState.value.dateOfFirstLearnSession = LocalDate("22-01-12")

        // When asking for date by X-axis
        val dateString = viewDeckViewModel.getDateForX(x)

        // Then the date is equal to the given date + 5 days
        assertEquals(expectedDate, dateString)
    }

    @ParameterizedTest(name = "{arguments}")
    @CsvSource(
        "2,     24-01-22",
        "-1,    03-01-22",
    )
    fun `getDateForX get date with week`(x: Int, expectedDate: String) {
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Week

        //given Date of the first learnsession
        viewDeckViewModel.viewDeckState.value.dateOfFirstLearnSession = LocalDate("22-01-12").withDayOfWeek(1)

        // When asking for date by X-axis
        val dateString = viewDeckViewModel.getDateForX(x)

        // Then the date is equal to the given date + 5 days
        assertEquals(expectedDate, dateString)
    }

    @ParameterizedTest(name = "{arguments}")
    @CsvSource(
        "2,     05-22",
        "-2,    01-22",
    )
    fun `getDateForX get date with month`(x: Int, expectedDate: String) {
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Month

        //given Date of the first learnsession
        viewDeckViewModel.viewDeckState.value.dateOfFirstLearnSession = LocalDate("22-03-01")

        // When asking for date by X-axis
        val dateString = viewDeckViewModel.getDateForX(x)

        // Then the date is equal to the given date + 5 days
        assertEquals(expectedDate, dateString)
    }

    @Test
    fun `setTimeSpendLine with timescale of day`(){
        //given timescale of day and 1 day with 1 minute of learning and a day later a day with 2 minutes of learning
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Day
        val minutesLearnedData = mutableMapOf<TimeScaleGraph, List<MinutesLearnedDTO>>()

        minutesLearnedData[TimeScaleGraph.Day] = listOf(
            MinutesLearnedDTO( 60, 1650528595583),
            MinutesLearnedDTO( 120, 1650628595583)
        )
        minutesLearnedData[TimeScaleGraph.Week] = listOf(MinutesLearnedDTO(3, 1650528595583))
        minutesLearnedData[TimeScaleGraph.Month] = listOf(MinutesLearnedDTO(4, 1650528595583))
        viewDeckViewModel.minutesLearned = minutesLearnedData

        //when
        viewDeckViewModel.setTimeSpendLine(viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph)

        //then check if 2 datapoints got added one x seperated
        assertEquals(listOf(DataPoint(0.0f, 60.0f), DataPoint(1.0f, 120.0f)) ,viewDeckViewModel.viewDeckState.value.minutesSpendPerX)
    }

    @Test
    fun `setTimeSpendLine with timescale of week`(){
        //given timescale of week and 1 week with 1 minute of learning and a week later a week with 2 minutes of learning
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Week
        val minutesLearnedData = mutableMapOf<TimeScaleGraph, List<MinutesLearnedDTO>>()

        minutesLearnedData[TimeScaleGraph.Day] = listOf(MinutesLearnedDTO(1, 1650528595583))
        minutesLearnedData[TimeScaleGraph.Week] = listOf(
            MinutesLearnedDTO(60, 1650528595583),
            MinutesLearnedDTO(120, 1651228595583),
            )
        minutesLearnedData[TimeScaleGraph.Month] = listOf(
            MinutesLearnedDTO(1, 1652128595583),)
        viewDeckViewModel.minutesLearned = minutesLearnedData

        //when
        viewDeckViewModel.setTimeSpendLine(viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph)

        //then check if 2 datapoints got added one x seperated
        assertEquals(listOf(DataPoint(0.0f, 60.0f), DataPoint(1.0f, 120.0f)), viewDeckViewModel.viewDeckState.value.minutesSpendPerX)
    }

    @Test
    fun `setTimeSpendLine with timescale of month`() {
        //given timescale of month and 1 month with 1 hour of learning and a month later a month with 2 minutes of learning
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Month
        val minutesLearnedData = mutableMapOf<TimeScaleGraph, List<MinutesLearnedDTO>>()

        minutesLearnedData[TimeScaleGraph.Day] = listOf(MinutesLearnedDTO(1000, 1650528595583))
        minutesLearnedData[TimeScaleGraph.Week] = listOf(MinutesLearnedDTO(1000, 1650528595583),)
        minutesLearnedData[TimeScaleGraph.Month] = listOf(
            MinutesLearnedDTO(600, 1650528595583),
            MinutesLearnedDTO(1200, 1652128595583),
        )
        viewDeckViewModel.minutesLearned = minutesLearnedData

        //when
        viewDeckViewModel.setTimeSpendLine(viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph)

        //then check if 2 datapoints got added one x seperated
        assertEquals(
            listOf(DataPoint(0.0f, 10.0f), DataPoint(1.0f, 20.0f)),
            viewDeckViewModel.viewDeckState.value.minutesSpendPerX
        )
    }

    @Test
    fun `setAvgScoreAllQuestionsLine with timescale of day`(){
        //given timescale of day and 1 day with score of 50% and a day later a day with a score of 100%
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Day
        val avgScoreData = mutableMapOf<TimeScaleGraph, List<AvgScoreDTO>>()

        avgScoreData[TimeScaleGraph.Day] = listOf(
            AvgScoreDTO(50.0, 1650528595583),
            AvgScoreDTO(100.0, 1650628595583)
        )
        avgScoreData[TimeScaleGraph.Week] = listOf(AvgScoreDTO(0.1, 1650528595583))
        avgScoreData[TimeScaleGraph.Month] = listOf(AvgScoreDTO(0.5, 1650528595583),)
        viewDeckViewModel.avgScoreQuestionsLine = avgScoreData

        //when
        viewDeckViewModel.setAvgScoreAllQuestionsLine(viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph)

        //then check if 2 datapoints got added one x seperated
        assertEquals(listOf(DataPoint(0.0f, 50f), DataPoint(1.0f, 100f)), viewDeckViewModel.viewDeckState.value.avgScoreQuestionsPerX)
    }

    @Test
    fun `setAvgScoreAllQuestionsLine with timescale of week`(){
        //given timescale of week and 1 week with score of 50% and a week later a week with a score of 100%
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Week
        val avgScoreData = mutableMapOf<TimeScaleGraph, List<AvgScoreDTO>>()

        avgScoreData[TimeScaleGraph.Day] = listOf(AvgScoreDTO(0.1, 1650528595583))
        avgScoreData[TimeScaleGraph.Week] = listOf(
            AvgScoreDTO(50.0, 1650528595583),
            AvgScoreDTO(100.0, 1651228595583)
        )
        avgScoreData[TimeScaleGraph.Month] = listOf(AvgScoreDTO(0.1, 1650528595583),)
        viewDeckViewModel.avgScoreQuestionsLine = avgScoreData

        //when
        viewDeckViewModel.setAvgScoreAllQuestionsLine(viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph)

        //then check if 2 datapoints got added one x seperated
        assertEquals(listOf(DataPoint(0.0f, 50f), DataPoint(1.0f, 100f)), viewDeckViewModel.viewDeckState.value.avgScoreQuestionsPerX)
    }

    @Test
    fun `setAvgScoreAllQuestionsLine with timescale of month`(){
        //given timescale of month and 1 month with score of 50% and a month later a month with a score of 100%
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Month
        val avgScoreData = mutableMapOf<TimeScaleGraph, List<AvgScoreDTO>>()

        avgScoreData[TimeScaleGraph.Day] = listOf(AvgScoreDTO( 0.1, 1650528595583))
        avgScoreData[TimeScaleGraph.Week] = listOf(AvgScoreDTO( 0.1, 1650528595583))
        avgScoreData[TimeScaleGraph.Month] = listOf(
            AvgScoreDTO( 50.0, 1650528595583),
            AvgScoreDTO( 100.0, 1652128595583),
        )
        viewDeckViewModel.avgScoreQuestionsLine = avgScoreData

        //when
        viewDeckViewModel.setAvgScoreAllQuestionsLine(viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph)

        //then check if 2 datapoints got added one x seperated
        assertEquals(listOf(DataPoint(0.0f, 50f), DataPoint(1.0f, 100f)), viewDeckViewModel.viewDeckState.value.avgScoreQuestionsPerX)
    }

    @Test
    fun `setAvgScoreLine with timescale of day`(){
        //given timescale of day and 1 day with score of 50% and a day later a day with a score of 100%
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Day
        val avgScoreData = mutableMapOf<TimeScaleGraph, List<AvgScoreDTO>>()

        avgScoreData[TimeScaleGraph.Day] = listOf(
            AvgScoreDTO( 50.0, 1650528595583),
            AvgScoreDTO( 100.0, 1650628595583)
        )
        avgScoreData[TimeScaleGraph.Week] = listOf(AvgScoreDTO( 0.1, 1650528595583))
        avgScoreData[TimeScaleGraph.Month] = listOf(AvgScoreDTO(0.5, 1650528595583),)
        viewDeckViewModel.avgScoresLine = avgScoreData

        //when
        viewDeckViewModel.setAvgScoreLine(viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph)

        //then check if 2 datapoints got added one x seperated
        assertEquals(listOf(DataPoint(0.0f, 50f), DataPoint(1.0f, 100f)), viewDeckViewModel.viewDeckState.value.avgScorePerX)
    }

    @Test
    fun `setAvgScoreLine with timescale of week`(){
        //given timescale of week and 1 week with score of 50% and a week later a week with a score of 100%
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Week
        val avgScoreData = mutableMapOf<TimeScaleGraph, List<AvgScoreDTO>>()

        avgScoreData[TimeScaleGraph.Day] = listOf(AvgScoreDTO( 0.1, 1650528595583))
        avgScoreData[TimeScaleGraph.Week] = listOf(
            AvgScoreDTO( 50.0, 1650528595583),
            AvgScoreDTO( 100.0, 1651228595583)
        )
        avgScoreData[TimeScaleGraph.Month] = listOf(AvgScoreDTO( 0.1, 1650528595583),)
        viewDeckViewModel.avgScoresLine = avgScoreData

        //when
        viewDeckViewModel.setAvgScoreLine(viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph)

        //then check if 2 datapoints got added one x seperated
        assertEquals(listOf(DataPoint(0.0f, 50f), DataPoint(1.0f, 100f)), viewDeckViewModel.viewDeckState.value.avgScorePerX)
    }

    @Test
    fun `setAvgScoreLine with timescale of month`(){
        //given timescale of month and 1 month with score of 50% and a month later a month with a score of 100%
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Month
        val avgScoreData = mutableMapOf<TimeScaleGraph, List<AvgScoreDTO>>()

        avgScoreData[TimeScaleGraph.Day] = listOf(AvgScoreDTO(0.1, 1650528595583))
        avgScoreData[TimeScaleGraph.Week] = listOf(AvgScoreDTO(0.1, 1650528595583))
        avgScoreData[TimeScaleGraph.Month] = listOf(
            AvgScoreDTO( 50.0, 1650528595583),
            AvgScoreDTO( 100.0, 1652128595583),
        )
        viewDeckViewModel.avgScoresLine = avgScoreData

        //when
        viewDeckViewModel.setAvgScoreLine(viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph)

        //then check if 2 datapoints got added one x seperated
        assertEquals(listOf(DataPoint(0.0f, 50f), DataPoint(1.0f, 100f)), viewDeckViewModel.viewDeckState.value.avgScorePerX)
    }


    @Test
    fun `get symbol for graph Question`(){
        //given question graph
        viewDeckViewModel.viewDeckState.value.selectedProgressGraph = ProgressGraph.Questions

        //when
        val symbol = viewDeckViewModel.getGraphSymbol()

        //then check if symbol is %
        assertEquals("%", symbol)
    }

    @Test
    fun `get symbol for graph Score`(){
        //given question graph
        viewDeckViewModel.viewDeckState.value.selectedProgressGraph = ProgressGraph.Score

        //when
        val symbol = viewDeckViewModel.getGraphSymbol()

        //then check if symbol is %
        assertEquals("%", symbol)
    }

    @Test
    fun `get symbol for graph Time (week-day)`(){
        //given question graph and timeScale of week
        viewDeckViewModel.viewDeckState.value.selectedProgressGraph = ProgressGraph.Time
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Week

        //when
        val symbol = viewDeckViewModel.getGraphSymbol()

        //then check if symbol is min
        assertEquals("min", symbol)
    }

    @Test
    fun `get symbol for graph Time (month)`(){
        //given question graph and timeScale of month
        viewDeckViewModel.viewDeckState.value.selectedProgressGraph = ProgressGraph.Time
        viewDeckViewModel.viewDeckState.value.selectedTimeScaleGraph = TimeScaleGraph.Month

        //when
        val symbol = viewDeckViewModel.getGraphSymbol()

        //then check if symbol is h
        assertEquals("h", symbol)
    }
}