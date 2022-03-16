package neet.code.flashwear.feature_learn_session.presentation.start_learn_session

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import neet.code.flashwear.Screen
import neet.code.flashwear.feature_learn_session.domain.model.LearnSession
import neet.code.flashwear.feature_learn_session.domain.use_case.LearnSessionUseCases
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import java.lang.Double.max
import java.lang.Double.min
import javax.inject.Inject


@HiltViewModel
class StartLearnSessionViewModel @Inject constructor(
    private val learnSessionUseCases: LearnSessionUseCases,
    private val questionsUseCases: QuestionsUseCases,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _learnSession = mutableStateOf(StartLearnSessionState())
    val learnSession: State<StartLearnSessionState> = _learnSession

    //create list of booleans to keep track of correctly answered questions
    private val currentBatchCorrectlyAnswered: MutableList<Boolean> = mutableListOf()

    //Used to calculate the final score of the learnsession
    private var learnSessionCorrect: Double = 0.0
    private var learnSessionTotalAnswered: Double = 0.0

    init {
        viewModelScope.launch {
            try{
                savedStateHandle.get<Int>("deckId")?.let { deckId ->
                    _learnSession.value = learnSession.value.copy(
                        learnSession = LearnSession(
                            deckId = deckId
                        ),
                    )
                }

                getAllQuestionsBatched()
                getEightQuestions()

            }
            catch (e: Exception){
                Log.i(TAG, "Exception in init: $e")
            }
        }
    }


    fun onEvent(event: StartLearnSessionEvent){
        when (event) {
            is StartLearnSessionEvent.ShowAnswer -> {
                _learnSession.value = learnSession.value.copy(
                    showAnswer = !learnSession.value.showAnswer
                )
            }
            is StartLearnSessionEvent.EnteredIfCorrect -> {
                _learnSession.value = learnSession.value.copy(
                     answeredQuestion = !learnSession.value.showAnswer
                )
            }

            is StartLearnSessionEvent.NextQuestion -> {

                submitAnswer(event.value)

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
                newScore = _learnSession.value.currentQuestion?.score?.times(1.25)
                    ?.let { min(it.round(),1.0) }

                //set the question to answered correctly true, based on the index of the question in currentQuestionsBatch
                currentBatchCorrectlyAnswered[_learnSession.value.currentQuestionsBatch.indexOf(_learnSession.value.currentQuestion)] = true
            }
            "=" -> {
                //score = score * 95% (subtract 5% from the score)
                learnSessionCorrect += 0.5
                newScore = _learnSession.value.currentQuestion?.score?.times(0.90)?.round()
                    ?.let { max(it, 0.0999) }
            }
            "-" -> {
                //score = score * 80% (subtract 20% from the score)
                newScore = _learnSession.value.currentQuestion?.score?.times(0.75)?.round()
                    ?.let { max(it, 0.0999) }
            }
        }

        //update the question in the currentQuestionBatch list with the new changed score
        _learnSession.value.currentQuestionsBatch[_learnSession.value.currentQuestionsBatch.indexOf(_learnSession.value.currentQuestion)].score = newScore

        setNextQuestion()
    }

    private fun setNextQuestion() {
        _learnSession.value = learnSession.value.copy(
            currentQuestionIndex = _learnSession.value.currentQuestionIndex + 1
        )

        var question: Question? = null

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
        }else{
            viewModelScope.launch {
                _learnSession.value.learnSession?.let { learnSessionUseCases.updateLearnSession(it) }
            }
        }

        //update the learnSession
        _learnSession.value.learnSession?.timeEnd = System.currentTimeMillis()
        _learnSession.value.learnSession?.score = calculateLearnSessionScore()
    }

    private fun calculateLearnSessionScore(): Double{
        return (learnSessionCorrect * 100 / learnSessionTotalAnswered).round()
    }

    private fun Double.round(): Double = String.format("%.3f", this).toDouble()


    private suspend fun getAllQuestionsBatched(){

        val questions = learnSession.value.learnSession?.let { questionsUseCases.getQuestionsWithDeck(it.deckId)}

        if (questions != null) {
            _learnSession.value = learnSession.value.copy(
                allQuestionsBatch = questions.chunked(8)
            )
        }
    }

    private fun getEightQuestions() {
        currentBatchCorrectlyAnswered.clear()

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
}


