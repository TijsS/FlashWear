package neet.code.flashwear.feature_deck.presentation.view_deck

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ViewDeckViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val questionsUseCases: QuestionsUseCases
): ViewModel() {

    private val _viewDeckState = mutableStateOf(ViewDeckState())
    val viewDeckState: State<ViewDeckState> = _viewDeckState

    private var recentlyDeleted: Question? = null
    private val categories = DeckQuestionCategory.values().asList()

    init {

        savedStateHandle.get<Int>("deckId")?.let { deckId ->
            _viewDeckState.value = viewDeckState.value.copy(
                deckId = deckId,
                categories = categories,
                selectedTab = DeckQuestionCategory.Questions
            )
        }

        getDecks()
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
                    Log.i(TAG, "onEvent: ${event.question.questionTitle}")
                    questionsUseCases.deleteQuestion(event.question)
                    recentlyDeleted = event.question
                }
            }
            is ViewDeckEvent.RestoreQuestion -> {
                viewModelScope.launch {
                    recentlyDeleted?.let { questionsUseCases.addQuestion(it) }
                    recentlyDeleted = null
                }
            }
            is ViewDeckEvent.ImportQuestions -> {
                var message: String
                viewModelScope.launch {
                    try {
                        questionsUseCases.addQuestions(event.importedQuestions)
                        message = "Added questions"
                    } catch (exception: Exception) {
                        message = "something went wrong"
                    }
                    _viewDeckState.value = viewDeckState.value.copy(
                        showImportedQuestionsMessage = true,
                        importedQuestionsMessage = message
                    )
                }
            }
        }
    }

    private fun importQuestionsOfFile(csvPath: String){

    }

    private fun getDecks(){
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
}

