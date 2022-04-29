package neet.code.flashwear.feature_deck.presentation.view_deck

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madrapps.plot.line.DataPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import neet.code.flashwear.feature_deck.domain.use_case.DecksUseCases
import neet.code.flashwear.feature_learn_session.domain.model.AvgScoreDTO
import neet.code.flashwear.feature_learn_session.domain.model.MinutesLearnedDTO
import neet.code.flashwear.feature_learn_session.domain.use_case.LearnSessionUseCases
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import neet.code.flashwear.feature_question.presentation.add_question.AddQuestionViewModel
import neet.code.flashwear.feature_settings.domain.model.LearnStyle
import neet.code.flashwear.feature_wearable.WearableUseCases
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.Months
import org.joda.time.Weeks
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class ViewDeckViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val questionsUseCases: QuestionsUseCases,
    private val decksUseCases: DecksUseCases,
    private val learnSessionUseCases: LearnSessionUseCases,
    private val wearableUseCases: WearableUseCases
): ViewModel() {

    private val _viewDeckState = mutableStateOf(ViewDeckState())
    val viewDeckState: State<ViewDeckState> = _viewDeckState

    private var recentlyDeleted: Question? = null

    lateinit var avgScoresLine: MutableMap<TimeScaleGraph, List<AvgScoreDTO>>
    lateinit var avgScoreQuestionsLine: MutableMap<TimeScaleGraph, List<AvgScoreDTO>>
    lateinit var minutesLearned: MutableMap<TimeScaleGraph, List<MinutesLearnedDTO>>

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        try {
            savedStateHandle.get<Int>("deckId")?.let { deckId ->
                _viewDeckState.value = viewDeckState.value.copy(
                    deckId = deckId,
                    categories = DeckQuestionCategory.values().asList(),
                    selectedTab = DeckQuestionCategory.Questions
                )
            }

            savedStateHandle.get<String>("deckName")?.let { deckName ->
                _viewDeckState.value = viewDeckState.value.copy(
                    deckName = deckName,
                )
            }

            getQuestions()

            viewModelScope.launch {
                avgScoresLine =
                    learnSessionUseCases.getAvgScoreByDeck(_viewDeckState.value.deckId.toLong())
                avgScoreQuestionsLine =
                    learnSessionUseCases.getAvgScoreQuestionsByDeck(_viewDeckState.value.deckId.toLong())
                minutesLearned =
                    learnSessionUseCases.getMinutesLearnedByDeck(_viewDeckState.value.deckId.toLong())

                setLines()
            }
        }catch (e: Exception){
            print(e)
        }
    }

    fun onEvent(event: ViewDeckEvent){
        when (event) {
            is ViewDeckEvent.ToggleActionMenu -> {
                //toggle the isFloatingMenuVisible state value
                _viewDeckState.value = viewDeckState.value.copy(
                    isFloatingMenuVisible = !viewDeckState.value.isFloatingMenuVisible
                )
            }
            is ViewDeckEvent.ToggleDeleteQuestion -> {
                //toggle the questionIsHeldForDelete state value
                _viewDeckState.value = viewDeckState.value.copy(
                    questionIsHeldForDelete = !viewDeckState.value.questionIsHeldForDelete
                )
            }
            is ViewDeckEvent.DeleteQuestion -> {
                viewModelScope.launch {
                    Log.i(TAG, "deleted question: ${event.question.questionTitle}")
                    questionsUseCases.deleteQuestion(event.question)
                    recentlyDeleted = event.question
                }
            }
            is ViewDeckEvent.DeleteDeck -> {
                viewModelScope.launch {
                    Log.i(TAG, "deleted deck")
                    decksUseCases.deleteDeck(_viewDeckState.value.deckId)
                    //TODO confirmation popup
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            message = "DeletedDeck"
                        )
                    )
                }
            }
            is ViewDeckEvent.RestoreQuestion -> {
                viewModelScope.launch {
                    recentlyDeleted?.let { questionsUseCases.addQuestion(it) }
                    recentlyDeleted = null
                }
            }
            is ViewDeckEvent.ImportQuestions -> {
                viewModelScope.launch {
                    try {
                        questionsUseCases.addQuestions(event.importedQuestions)
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message =  "${event.importedQuestions.size} questions imported"
                            )
                        )
                    } catch (exception: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message =  "Not able to import"
                            )
                        )
                    }
                }
            }
            is ViewDeckEvent.SelectProgressGraph -> {
                _viewDeckState.value = viewDeckState.value.copy(
                    selectedProgressGraph = event.selectedGraph
                )
            }
            is ViewDeckEvent.SelectedTimeScale -> {
                _viewDeckState.value = viewDeckState.value.copy(
                    selectedTimeScaleGraph = event.selectedTimeScale
                )
                setLines()
            }
            is ViewDeckEvent.SyncWithWearable -> {
                viewModelScope.launch {
                    val questions = questionsUseCases.getQuestionsWithDeck(
                        deckId = _viewDeckState.value.deckId,
                        LearnStyle.Random
                    )
                    wearableUseCases.syncQuestions(questions = questions, hardSync = true)

                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            message = "Synced"
                        )
                    )
                }
            }
        }
    }

    private fun getQuestions(){
        questionsUseCases.getQuestionsWithDeckFlow(viewDeckState.value.deckId)
            .onEach { questions ->
                _viewDeckState.value = viewDeckState.value.copy(
                    questions = questions
                )
            }
            .launchIn(viewModelScope)
    }

    fun onCategorySelected(category: DeckQuestionCategory) {
        _viewDeckState.value = viewDeckState.value.copy(
            selectedTab = category
        )
    }

    private fun setLines(){
        val selectedTimeScale = viewDeckState.value.selectedTimeScaleGraph
        //get Avg Score per selected period
        viewModelScope.launch {
            if(!avgScoresLine[TimeScaleGraph.Day].isNullOrEmpty()) {
                setAvgScoreLine(selectedTimeScale)
            }
        }

        //get Avg Score of all questions per selected period
        viewModelScope.launch {
            if(!avgScoresLine[TimeScaleGraph.Day].isNullOrEmpty()) {
                setAvgScoreAllQuestionsLine(selectedTimeScale)
            }
        }

        //Get amount of time spend learning per selected period
        viewModelScope.launch {
            if (!minutesLearned[TimeScaleGraph.Day].isNullOrEmpty()) {
                setTimeSpendLine(selectedTimeScale)
            }
        }
    }

    fun getDateForX(x: Int): String?{
        return when(viewDeckState.value.selectedTimeScaleGraph){
            TimeScaleGraph.Day -> {
                _viewDeckState.value.dateOfFirstLearnSession?.plusDays(x)?.toString("dd-MM-yy")
            }
            TimeScaleGraph.Week -> {
                _viewDeckState.value.dateOfFirstLearnSession?.plusWeeks(x)?.toString("dd-MM-yy")

            }
            TimeScaleGraph.Month -> {
                _viewDeckState.value.dateOfFirstLearnSession?.plusMonths(x)?.toString("MM-yy")
            }
        }
    }

    /*
    * converts: 12-04-2022;AvgScore = to DataPoint(x(days since first learnsession);AvgScore)
    */
    fun setAvgScoreLine(selectedTimeScale: TimeScaleGraph) {
        //get the first day this deck got used
        val dateOfFirstLearnSession = avgScoresLine[selectedTimeScale]?.first()
            ?.getLocalDate(selectedTimeScale)

        val datapoints: MutableList<DataPoint> = mutableListOf()

        for (learnSession in avgScoresLine[selectedTimeScale]!!) {
            val xAxis =
                dateToX(
                    dateOfFirstLearnSession = dateOfFirstLearnSession,
                    dateOfLearnSession = learnSession.getLocalDate(selectedTimeScale),
                    selectedTimeScale = selectedTimeScale
                )

            datapoints.add(
                DataPoint(
                    xAxis,
                    learnSession.score.round().toFloat()
                )
            )
        }
        _viewDeckState.value = viewDeckState.value.copy(
            avgScorePerX = datapoints,
            dateOfFirstLearnSession = dateOfFirstLearnSession,
        )
    }

    /*
    * converts: 12-04-2022;AvgScoreOfQuestions = to DataPoint(x(days since first learnsession);AvgScoreOfQuestions)
    */
    fun setAvgScoreAllQuestionsLine(selectedTimeScale: TimeScaleGraph) {
        //get the first day this deck got used
        val dateOfFirstLearnSession = avgScoreQuestionsLine[selectedTimeScale]?.first()
            ?.getLocalDate(selectedTimeScale)

        val datapoints: MutableList<DataPoint> = mutableListOf()

        for (learnSession in avgScoreQuestionsLine[selectedTimeScale]!!) {
            val xAxis =
                dateToX(
                    dateOfFirstLearnSession = dateOfFirstLearnSession,
                    dateOfLearnSession = learnSession.getLocalDate(selectedTimeScale),
                    selectedTimeScale = selectedTimeScale
                )

            datapoints.add(
                DataPoint(
                    xAxis,
                    learnSession.score.round().toFloat()
                )
            )
        }
        _viewDeckState.value = viewDeckState.value.copy(
            avgScoreQuestionsPerX = datapoints,
            dateOfFirstLearnSession = dateOfFirstLearnSession,
        )
    }

    private fun Double.round(): Double = BigDecimal(this).setScale(1, RoundingMode.HALF_EVEN).toDouble()

    /*
    * converts: 12-04-2022;timeSpend = to DataPoint(x(days since first learnsession);timespend)
    */
    fun setTimeSpendLine(selectedTimeScale: TimeScaleGraph) {
        //get the first day this deck got used
        val dateOfFirstLearnSession =
            minutesLearned[selectedTimeScale]?.first()
                ?.getLocalDate(selectedTimeScale)

        val datapoints: MutableList<DataPoint> = mutableListOf()

        for (learnSession in minutesLearned[selectedTimeScale]!!) {
            val xAxis =
                dateToX(
                    dateOfFirstLearnSession = dateOfFirstLearnSession,
                    dateOfLearnSession = learnSession.getLocalDate(selectedTimeScale),
                    selectedTimeScale = selectedTimeScale
                )

            //Add datapoint (days between start to current; in case of timescale day and week, minutes learned, in case of month hours learned)
            datapoints.add(
                DataPoint(
                    xAxis,
                    learnSession.minutesSpend.div(if(selectedTimeScale == TimeScaleGraph.Month) 60 else 1).toFloat()
                )
            )
        }

        _viewDeckState.value = viewDeckState.value.copy(
            minutesSpendPerX = datapoints,
            dateOfFirstLearnSession = dateOfFirstLearnSession
        )
    }

    private fun dateToX(dateOfFirstLearnSession: LocalDate?, dateOfLearnSession: LocalDate, selectedTimeScale: TimeScaleGraph): Float{
        return when(selectedTimeScale){
            TimeScaleGraph.Day -> {
                Days.daysBetween(
                    dateOfFirstLearnSession,
                    dateOfLearnSession
                ).days.toFloat()
            }
            TimeScaleGraph.Week -> {
                Weeks.weeksBetween(
                    dateOfFirstLearnSession,
                    dateOfLearnSession
                ).weeks.toFloat()
            }
            TimeScaleGraph.Month -> {
                Months.monthsBetween(
                    dateOfFirstLearnSession,
                    dateOfLearnSession
                ).months.toFloat()
            }
        }
    }


    fun getGraphSymbol(): String{
        return when(viewDeckState.value.selectedProgressGraph){
            ProgressGraph.Questions -> {
                return "%"
            }
            ProgressGraph.Score -> {
                "%"
            }
            ProgressGraph.Time -> {
                if(viewDeckState.value.selectedTimeScaleGraph == TimeScaleGraph.Month) "h" else "min"
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
    }
}


