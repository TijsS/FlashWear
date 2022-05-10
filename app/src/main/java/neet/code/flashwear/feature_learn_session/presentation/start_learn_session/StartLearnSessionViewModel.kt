package neet.code.flashwear.feature_learn_session.presentation.start_learn_session

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
import neet.code.flashwear.feature_learn_session.domain.model.LearnSession
import neet.code.flashwear.feature_learn_session.domain.use_case.LearnSessionUseCases
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import neet.code.flashwear.feature_question.presentation.view_question.ViewQuestionViewModel
import java.lang.Double.max
import java.lang.Double.min
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class StartLearnSessionViewModel @Inject constructor(
    private val learnSessionUseCases: LearnSessionUseCases,
    private val questionsUseCases: QuestionsUseCases,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _learnSession = mutableStateOf(StartLearnSessionState())
    val learnSession: State<StartLearnSessionState> = _learnSession

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    //create list of booleans to keep track of correctly answered questions
    val currentBatchCorrectlyAnswered: MutableList<Boolean> = mutableListOf()

    //Used to calculate the final score of the learnsession
    var learnSessionCorrect = 0.0
    var learnSessionTotalAnswered = 0

    init {
        try{
            savedStateHandle.get<Int>("deckId")?.let { deckId ->
                _learnSession.value = learnSession.value.copy(
                    learnSession = LearnSession(
                         deckId = deckId,
                        timeStart = System.currentTimeMillis()
                    ),
                )
            }

            savedStateHandle.get<String>("deckName")?.let { deckName ->
                _learnSession.value = learnSession.value.copy(
                    deckName = deckName,
                )
            }

            viewModelScope.launch {
                    getAllQuestionsBatched()
                    getEightQuestions()
            }
        }
        catch (e: Exception){
            Log.i(TAG, "Exception in init: $e")
        }
    }


    fun onEvent(event: StartLearnSessionEvent){
        when (event) {

            is StartLearnSessionEvent.ShowAnswer -> {
                _learnSession.value = learnSession.value.copy(
                    showAnswer = !learnSession.value.showAnswer
                )
            }

            is StartLearnSessionEvent.NextQuestion -> {

                submitAnswer(event.value)
                setNextQuestion()

                _learnSession.value = learnSession.value.copy(
                    showAnswer = false,
                    answeredQuestion = false,
                )
            }
        }
    }


    private fun submitAnswer(value: String) {
        var newScore: Double? = null
        learnSessionTotalAnswered++
        when(value){
            "+" -> {
                learnSessionCorrect++
                //score = score * 115% (add 15% to the score)
                newScore = _learnSession.value.currentQuestion?.score?.times(CORRECT_MODIFIER)
                    ?.let { min(it.round(),1.0) }

                //set the question to answered correctly true, based on the index of the question in currentQuestionsBatch
                currentBatchCorrectlyAnswered[_learnSession.value.currentQuestionsBatch.indexOf(_learnSession.value.currentQuestion)] = true
            }
            "=" -> {
                //score = score * 95% (subtract 5% from the score)
                learnSessionCorrect += 0.5
                newScore = _learnSession.value.currentQuestion?.score?.times(HALF_CORRECT_MODIFIER)?.round()
                    ?.let { max(it, 0.0999) }
            }
            "-" -> {
                //score = score * 80% (subtract 20% from the score)
                newScore = _learnSession.value.currentQuestion?.score?.times(WRONG_MODIFIER)?.round()
                    ?.let { max(it, 0.0999) }
            }
        }

        //update the question in the currentQuestionBatch list with the new changed score
        if (newScore != null) {
            _learnSession.value.currentQuestionsBatch[_learnSession.value.currentQuestionsBatch.indexOf(_learnSession.value.currentQuestion)].score = newScore
        }
    }

    private fun setNextQuestion() {
        _learnSession.value = learnSession.value.copy(
            currentQuestionIndex = _learnSession.value.currentQuestionIndex + 1
        )

        val question: Question?

        if(_learnSession.value.currentQuestionIndex < _learnSession.value.currentQuestionsBatch.size){
            question = _learnSession.value.currentQuestionsBatch[_learnSession.value.currentQuestionIndex]
            _learnSession.value.currentQuestion = question
        }
        else{
            if(currentBatchCorrectlyAnswered.contains(false)){
                question = _learnSession.value.currentQuestionsBatch[randomWronglyAnsweredQuestion()]
                _learnSession.value.currentQuestion = question
            }
            else {
                publishAnswers()
                if(_learnSession.value.allQuestionsBatch.size == _learnSession.value.currentBatchIndex ){
                    learnSession.value.finished = true
                }else{
                    //when the list doesnt contain any more questions, save the progress and get new QuestionsBatch
                    getEightQuestions()
                }
            }
        }
    }

    private fun randomWronglyAnsweredQuestion(): Int {
        return currentBatchCorrectlyAnswered.indexOf(false)
    }


    private fun publishAnswers(){
        //update the questions (scores) in the db
        for(question in _learnSession.value.currentQuestionsBatch){
            viewModelScope.launch {
                questionsUseCases.updateQuestion(question)
            }
        }

        if(learnSession.value.currentBatchIndex == 1){
            viewModelScope.launch {
                val newLearnSessionId: Long? = _learnSession.value.learnSession?.let { learnSessionUseCases.startLearnSession(it) }
                if (newLearnSessionId != null) {
                    _learnSession.value.learnSession?.id = newLearnSessionId.toInt()
                }
            }
        }

        //update the learnSession
        viewModelScope.launch {
            _learnSession.value.learnSession?.timeEnd = System.currentTimeMillis()
            _learnSession.value.learnSession?.score = calculateLearnSessionScore()
            _learnSession.value.learnSession?.let { learnSessionUseCases.updateLearnSession(it) }
        }
    }

    private fun calculateLearnSessionScore(): Double{
        return (learnSessionCorrect * 100 / learnSessionTotalAnswered).round()
    }

    private fun Double.round(): Double = BigDecimal(this).setScale(2, RoundingMode.HALF_EVEN).toDouble()

    private suspend fun getAllQuestionsBatched(){
        val questions = learnSession.value.learnSession?.let { questionsUseCases.getQuestionsWithDeck(it.deckId)}

        if (questions != null) {
            //if the list contains no questions return to main screen
            if (questions.isEmpty()){
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            message = "No questions available"
                        )
                    )
                }
                learnSession.value.finished = true
            }
            _learnSession.value = learnSession.value.copy(
                allQuestionsBatch = questions.chunked(8)
            )
        }
    }

    private fun getEightQuestions() {
        currentBatchCorrectlyAnswered.clear()

        //for each question add 'tracker' if answered correctly
        if(_learnSession.value.allQuestionsBatch.isEmpty()){
            learnSession.value.finished = true
            return
        }
        repeat(_learnSession.value.allQuestionsBatch[_learnSession.value.currentBatchIndex].size){
            currentBatchCorrectlyAnswered.add(false)
        }

        _learnSession.value = learnSession.value.copy(
            currentQuestionsBatch = _learnSession.value.allQuestionsBatch[_learnSession.value.currentBatchIndex],
            currentQuestion = _learnSession.value.allQuestionsBatch[_learnSession.value.currentBatchIndex][0],
            currentQuestionIndex = 0,
            currentBatchIndex = _learnSession.value.currentBatchIndex + 1,
        )
    }

    companion object {
        const val CORRECT_MODIFIER = 1.25
        const val HALF_CORRECT_MODIFIER = 0.9
        const val WRONG_MODIFIER = 0.75
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
    }
}


