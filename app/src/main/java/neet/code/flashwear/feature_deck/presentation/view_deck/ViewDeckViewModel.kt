package neet.code.flashwear.feature_deck.presentation.view_deck

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import neet.code.flashwear.R
import neet.code.flashwear.feature_deck.domain.use_case.DecksUseCases
import neet.code.flashwear.feature_learn_session.domain.use_case.LearnSessionUseCases
import neet.code.flashwear.feature_progress.presentation.progress.ProgressGraph
import neet.code.flashwear.feature_progress.presentation.progress.TimeScaleGraph
import neet.code.flashwear.feature_question.domain.model.InvalidQuestionException
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import neet.code.flashwear.feature_settings.domain.model.LearnStyle
import neet.code.flashwear.feature_wearable.WearableUseCases
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

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        try {
            savedStateHandle.get<Int>("deckId")?.let { deckId ->
                _viewDeckState.value = viewDeckState.value.copy(
                    deckId = deckId,
                    categories = DeckCategory.values().asList(),
                    selectedTab = DeckCategory.Questions
                )
            }

            savedStateHandle.get<String>("deckName")?.let { deckName ->
                _viewDeckState.value = viewDeckState.value.copy(
                    deckName = deckName,
                )
            }

            getQuestions()

            viewModelScope.launch {
                val pair = learnSessionUseCases.getAvgScoreByDeck(_viewDeckState.value.deckId.toLong())

                _viewDeckState.value = viewDeckState.value.copy(
                    avgScoresLine = pair.first,
                    dateOfFirstLearnSession = pair.second
                )
                _viewDeckState.value = viewDeckState.value.copy(
                    avgScoreQuestionsLine = learnSessionUseCases.getAvgScoreQuestionsByDeck(_viewDeckState.value.deckId.toLong())
                )
                _viewDeckState.value = viewDeckState.value.copy(
                    minutesLearnedLine = learnSessionUseCases.getMinutesLearnedByDeck(_viewDeckState.value.deckId.toLong())
                )
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
            is ViewDeckEvent.ToggleDeleteDeckBox -> {
                //toggle the showDeleteDeckBox state value
                _viewDeckState.value = viewDeckState.value.copy(
                    showDeleteDeckBox = !viewDeckState.value.showDeleteDeckBox
                )
            }
            is ViewDeckEvent.CloseDeleteBox -> {
                //toggle the showDeleteDeckBox state value
                _viewDeckState.value = viewDeckState.value.copy(
                    showDeleteDeckBox = false
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
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            baseMessage = R.string.deleted_deck
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
                                message = event.importedQuestions.size.toString(),
                                baseMessage = R.string.imported
                            )
                        )
                    }
                    catch(invalidQuestion: InvalidQuestionException){
                        val exception = invalidQuestion.message?.split("|")
                        if (exception != null){
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar(
                                    message = exception[0],
                                    baseMessage = exception[1].toInt(),
                                    duration = SnackbarDuration.Long
                                )
                            )
                        }
                    }
                    catch (exception: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                baseMessage =  R.string.csv_import_fail
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
                            baseMessage = R.string.synced
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

    fun onCategorySelected(category: DeckCategory) {
        _viewDeckState.value = viewDeckState.value.copy(
            selectedTab = category
        )
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

    fun getGraphSymbol(): String{
        return when(viewDeckState.value.selectedProgressGraph){
            ProgressDeckGraph.Questions -> {
                return "%"
            }
            ProgressDeckGraph.Score -> {
                "%"
            }
            ProgressDeckGraph.Time -> {
                if(viewDeckState.value.selectedTimeScaleGraph == TimeScaleGraph.Month) "h" else "min"
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String = "", val baseMessage: Int, val duration: SnackbarDuration = SnackbarDuration.Short): UiEvent()
    }
}


