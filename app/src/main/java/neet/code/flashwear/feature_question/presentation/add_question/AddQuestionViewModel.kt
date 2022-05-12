package neet.code.flashwear.feature_question.presentation.add_question

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import neet.code.flashwear.feature_deck.presentation.view_deck.ViewDeckViewModel
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.model.InvalidQuestionException
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import javax.inject.Inject

@HiltViewModel
class AddQuestionViewModel @Inject constructor(
    private val questionUseCases: QuestionsUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _questionState = mutableStateOf(AddQuestionState())
    val questionState: State<AddQuestionState> = _questionState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("deckId")?.let { deckId ->
            _questionState.value = questionState.value.copy(
                deckId = deckId
            )
        }
    }

    fun onEvent(event: AddQuestionEvent){
        when (event) {
            is AddQuestionEvent.EnteredQuestionTitle -> {
                _questionState.value = questionState.value.copy(
                    questionTitle = event.value
                )
            }
            is AddQuestionEvent.EnteredQuestionContent -> {
                _questionState.value = questionState.value.copy(
                    questionContent = event.value
                )
            }
            is AddQuestionEvent.EnteredAnswerTitle -> {
                _questionState.value = questionState.value.copy(
                    answerTitle = event.value
                )
            }
            is AddQuestionEvent.EnteredAnswerContent -> {
                _questionState.value = questionState.value.copy(
                    answerContent = event.value
                )
            }
            is AddQuestionEvent.EnteredAnswerSub -> {
                _questionState.value = questionState.value.copy(
                    answerSub = event.value
                )
            }
            is AddQuestionEvent.AddQuestion -> {
                viewModelScope.launch {
                    try {
                        questionUseCases.addQuestion(
                            question = Question(
                                questionTitle = questionState.value.questionTitle,
                                questionContent = questionState.value.questionContent,
                                answerTitle = questionState.value.answerTitle,
                                answerContent = questionState.value.answerContent,
                                answerSub = questionState.value.answerSub,
                                deckId = questionState.value.deckId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveQuestion)
                    } catch(e: InvalidQuestionException) {
                        val exception = e.message?.split("|")
                        if (exception != null) {
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar(
                                    message = exception[0],
                                    baseMessage = exception[1].toInt(),
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String = "", val baseMessage: Int): UiEvent()
        object SaveQuestion: UiEvent()
    }
}