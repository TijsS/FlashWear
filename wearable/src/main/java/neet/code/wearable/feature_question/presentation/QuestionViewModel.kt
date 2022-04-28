package neet.code.wearable.feature_question.presentation

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import neet.code.wearable.feature_deck.presentation.LearnStyle
import neet.code.wearable.feature_question.domain.Question
import neet.code.wearable.feature_question.domain.use_case.QuestionsUseCases
import javax.inject.Inject


@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionsUseCases: QuestionsUseCases,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _question = mutableStateOf(QuestionState())
    val question: State<QuestionState> = _question

    init {
        viewModelScope.launch {
            try{
                savedStateHandle.get<Int>("deckId")?.let { deckId ->
                    _question.value = question.value.copy(
                        deckId = deckId,
                    )
                }

                savedStateHandle.get<LearnStyle>("learnStyle")?.let { learnStyle ->
                    _question.value = question.value.copy(
                        learnStyle = learnStyle,
                    )
                }

                getAllQuestions()
                setNextQuestion()
            }
            catch (e: Exception){
                Log.i(TAG, "Exception in init: $e")
            }
        }
    }


    fun onEvent(event: QuestionEvent){
        when (event) {
            is QuestionEvent.ShowAnswer -> {
                _question.value = question.value.copy(
                    showAnswer = !question.value.showAnswer
                )
            }
            is QuestionEvent.NextQuestion -> {
                _question.value = question.value.copy(
                    showAnswer = false,
                    answeredQuestion = false,
                )

                val questionId = _question.value.currentQuestion?.id

                viewModelScope.launch {
                    if (questionId != null) {
                        submitAnswer(event.value, questionId)
                    }
                }
            }
        }
    }


    private suspend fun submitAnswer(value: String, questionId: Int) {
        questionsUseCases.sendQuestionResult(
            result = value,
            questionId = questionId
        )

        setNextQuestion()
    }

    private fun setNextQuestion() {

        //increase question index by 1
        val newQuestionIndex = _question.value.currentQuestionIndex + 1
        _question.value = question.value.copy(
            currentQuestionIndex = newQuestionIndex
        )

        //if the final question is not yet reached
        if(newQuestionIndex < _question.value.allQuestions.size){
            _question.value.currentQuestion = _question.value.allQuestions[newQuestionIndex]
        }
        else{
            _question.value.finished = true
        }
    }

    private suspend fun getAllQuestions(){
        val deckId = question.value.deckId
        val learnStyle = question.value.learnStyle

        val questions = questionsUseCases.getQuestionsWithDeck(deckId, learnStyle)

        if (questions.isNotEmpty()) {
            _question.value = question.value.copy(
                allQuestions = questions
            )
        }
    }
}
