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