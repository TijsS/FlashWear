package neet.code.flashwear.viewmodels


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import io.mockk.coEvery
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import neet.code.flashwear.feature_learn_session.domain.use_case.LearnSessionUseCases
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.StartLearnSessionEvent
import neet.code.flashwear.feature_learn_session.presentation.start_learn_session.StartLearnSessionViewModel
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@ExtendWith(MockKExtension::class)
class StartLearnSessionViewModelTest {

    private lateinit var viewModel: StartLearnSessionViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @JvmField
    @RegisterExtension
    var mainCoroutineExtension = MainCoroutineExtension()

    val question1 = Question(
        id = 1,
        questionTitle = "test1a",
        questionContent = "test1b",
        answerTitle = "test1c",
        answerContent = "test1d",
        answerSub = "test1e",
        score = 0.9,
        deckId = 1
    )

    val question2 = Question(
        id = 2,
        questionTitle = "test2a",
        questionContent = "test2b",
        answerTitle = "test2c",
        answerContent = "test2d",
        answerSub = "test2e",
        score = 0.4,
        deckId = 1
    )

    val questions = listOf(
        question1,
        question2,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun before() {
        //mock the logging
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        val learnSessionUseCases = mockk<LearnSessionUseCases>()
        val questionsUseCases = mockk<QuestionsUseCases>()
        coEvery { questionsUseCases.getQuestionsWithDeck(1) } returns questions


        val savedStateHandle = SavedStateHandle().apply {
            set("deckId", 1)
            set("deckName", "deck1")
        }

        viewModel = StartLearnSessionViewModel(
            learnSessionUseCases = learnSessionUseCases,
            questionsUseCases = questionsUseCases,
            savedStateHandle
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `submit correct answer`() {
        //given viewmodel init is finished (2 questions get added)
        mainCoroutineExtension.scheduler.advanceUntilIdle()

        //when
        viewModel.onEvent(StartLearnSessionEvent.NextQuestion("+"))

        //then
        assertAll("correct answer",
            { assertEquals(1, viewModel.learnSession.value.currentQuestionIndex) },
            { assertEquals(false, viewModel.learnSession.value.showAnswer) },
            { assertEquals(1, viewModel.learnSession.value.currentBatchIndex) },
            { assertEquals(question2, viewModel.learnSession.value.currentQuestion) },
            { assertEquals(listOf(true, false), viewModel.currentBatchCorrectlyAnswered) },
            { assertEquals(1.0, viewModel.learnSessionCorrect, 0.0) },
            { assertEquals(1, viewModel.learnSessionTotalAnswered) },
            { assertEquals(1.0, viewModel.learnSession.value.currentQuestionsBatch[0].score) },
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `submit half correct answer`() {
/*
        given viewmodel init is finished (2 questions get added) change score of question to 0.1
        this way we can test if the minimum cap of 0.0999 works
*/
        mainCoroutineExtension.scheduler.advanceUntilIdle()
        viewModel.learnSession.value.currentQuestionsBatch[0].score = 0.1

        //when
        viewModel.onEvent(StartLearnSessionEvent.NextQuestion("="))

        //then
        assertAll("half correct answer",
            { assertEquals(1, viewModel.learnSession.value.currentQuestionIndex) },
            { assertEquals(false, viewModel.learnSession.value.showAnswer) },
            { assertEquals(1, viewModel.learnSession.value.currentBatchIndex) },
            { assertEquals(question2, viewModel.learnSession.value.currentQuestion) },
            { assertEquals(listOf(false, false), viewModel.currentBatchCorrectlyAnswered) },
            { assertEquals(0.5, viewModel.learnSessionCorrect, 0.0) },
            { assertEquals(1, viewModel.learnSessionTotalAnswered) },
            { assertEquals(0.0999, viewModel.learnSession.value.currentQuestionsBatch[0].score) },
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `end of batch get wrongly answered question again`() {
/*
        given viewmodel init is finished (2 questions get added)
        and one wrongly answered question is submitted
*/
        mainCoroutineExtension.scheduler.advanceUntilIdle()
        viewModel.onEvent(StartLearnSessionEvent.NextQuestion("-"))

        //when final question of batch get answered
        viewModel.onEvent(StartLearnSessionEvent.NextQuestion("+"))

        //then check that the wrongly answered question (question1) is set as current question
        assertAll("wrong answer repeat",
            { assertEquals(question1, viewModel.learnSession.value.currentQuestion)},
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `end of question deck`() {
/*
        given viewmodel init is finished (2 questions get added)
        2 questions get correctly submitted
*/
        mainCoroutineExtension.scheduler.advanceUntilIdle()
        viewModel.onEvent(StartLearnSessionEvent.NextQuestion("+"))

        //when final question get answered
        viewModel.onEvent(StartLearnSessionEvent.NextQuestion("+"))

        //then check that the wrongly answered question (question1) is set as current question
        assertAll("wrong answer repeat",
            { assertEquals(true, viewModel.learnSession.value.finished)},
        )
    }
}