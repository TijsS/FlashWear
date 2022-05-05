package neet.code.flashwear.feature_question.presentation.view_question

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import neet.code.flashwear.Screen
import neet.code.flashwear.feature_deck.presentation.add_deck.AddDeckViewModel
import neet.code.flashwear.feature_question.domain.model.InvalidQuestionException
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import javax.inject.Inject

@HiltViewModel
class ViewQuestionViewModel @Inject constructor(
    private val questionUseCases: QuestionsUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _questionState = mutableStateOf(ViewQuestionState())
    val questionState: State<ViewQuestionState> = _questionState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            savedStateHandle.get<Int>("questionId")?.let { questionId ->
                _questionState.value = questionState.value.copy(
                    question = questionUseCases.getQuestionById(questionId)
                )
                //display as 1-100 instead of 0.1 - 1.0
                _questionState.value = questionState.value.copy(
                    question = _questionState.value.question?.score?.let { questionState.value.question?.copy(score = it.times(100)) }
                )
            }
        }
    }

    fun onEvent(event: ViewQuestionEvent){
        when (event) {
            is ViewQuestionEvent.EnteredQuestionTitle -> {
                _questionState.value = questionState.value.copy(
                    question = questionState.value.question?.copy(questionTitle = event.value)
                )
            }
            is ViewQuestionEvent.EnteredQuestionContent -> {
                _questionState.value = questionState.value.copy(
                    question = questionState.value.question?.copy(questionContent = event.value)
                )
            }
            is ViewQuestionEvent.EnteredAnswerTitle -> {
                _questionState.value = questionState.value.copy(
                    question = questionState.value.question?.copy(answerTitle = event.value)
                )
            }
            is ViewQuestionEvent.EnteredAnswerContent -> {
                _questionState.value = questionState.value.copy(
                    question = questionState.value.question?.copy(answerContent = event.value)
                )
            }
            is ViewQuestionEvent.EnteredAnswerSub -> {
                _questionState.value = questionState.value.copy(
                    question = questionState.value.question?.copy(answerSub = event.value)
                )
            }
            is ViewQuestionEvent.EnteredScore -> {
                _questionState.value = questionState.value.copy(
                    question = questionState.value.question?.copy(score = event.value.toDouble())
                )
            }
            is ViewQuestionEvent.UpdateQuestion -> {
                viewModelScope.launch {
                    try {
                        questionState.value.question?.let {
                            questionUseCases.updateQuestion(
                                question = it.copy(score = it.score.div(100))
                            )
                        }
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = "updated question"
                            )
                        )
                        _eventFlow.emit(
                            UiEvent.UpdateQuestion
                        )
                    } catch(e: InvalidQuestionException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't update question"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object UpdateQuestion: UiEvent()
    }
}