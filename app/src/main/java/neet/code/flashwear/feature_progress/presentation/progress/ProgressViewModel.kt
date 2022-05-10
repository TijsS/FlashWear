package neet.code.flashwear.feature_progress.presentation.progress

import android.util.Log
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import neet.code.flashwear.feature_learn_session.domain.use_case.LearnSessionUseCases
import javax.inject.Inject


@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val learnSessionUseCases: LearnSessionUseCases,
): ViewModel() {

    private val _progressState = mutableStateOf(ProgressState())
    val progressState: State<ProgressState> = _progressState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        try {
            viewModelScope.launch {
                val pair = learnSessionUseCases.getTimeSpentTotal()
                _progressState.value = progressState.value.copy(
                    timeSpentTotal = pair.first,
                    dateOfFirstLearnSession = pair.second
                )
            }
        }catch (e: Exception){
            print(e)
        }
    }

    fun onEvent(event: ProgressEvent){
        when (event) {

            is ProgressEvent.SelectProgressGraph -> {
                _progressState.value = progressState.value.copy(
                    selectedProgressGraph = event.selectedGraph
                )
            }
            is ProgressEvent.SelectedTimeScale -> {
                _progressState.value = progressState.value.copy(
                    selectedTimeScaleGraph = event.selectedTimeScale
                )
            }
        }
    }

    fun getDateForX(x: Int): String?{
        return when(progressState.value.selectedTimeScaleGraph){
            TimeScaleGraph.Day -> {
                _progressState.value.dateOfFirstLearnSession?.plusDays(x)?.toString("dd-MM-yy")
            }
            TimeScaleGraph.Week -> {
                _progressState.value.dateOfFirstLearnSession?.plusWeeks(x)?.toString("dd-MM-yy")

            }
            TimeScaleGraph.Month -> {
                _progressState.value.dateOfFirstLearnSession?.plusMonths(x)?.toString("MM-yy")
            }
        }
    }

    fun getGraphSymbol(): String{
        return when(progressState.value.selectedProgressGraph){
            ProgressGraph.Time -> {
                if(progressState.value.selectedTimeScaleGraph == TimeScaleGraph.Month) "h" else "min"
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String, val duration: SnackbarDuration = SnackbarDuration.Short): UiEvent()
    }
}


