package neet.code.flashwear.feature_settings.presentation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import neet.code.flashwear.feature_deck.domain.util.DeckOrder
import neet.code.flashwear.feature_deck.domain.util.OrderType
import neet.code.flashwear.feature_deck.presentation.decks.DeckViewModel
import neet.code.flashwear.feature_question.domain.model.Question
import neet.code.flashwear.feature_question.domain.model.InvalidQuestionException
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import neet.code.flashwear.feature_settings.domain.model.LearnStyle
import neet.code.flashwear.feature_settings.domain.model.Settings
import neet.code.flashwear.feature_settings.domain.use_case.SettingsUseCases
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
    ): ViewModel() {

    private val _settingsState = mutableStateOf(SettingsState(Settings(), LearnStyle.New))
    val settingsState: State<SettingsState> = _settingsState

    private var getSettingsJob: Job? = null

    init {
        getSettings()
    }

    fun onEvent(event: SettingsEvent){
        when (event) {
            is SettingsEvent.ChangeLearnStyle -> {
                val newSettings = settingsState.value.settings
                newSettings.learnStyle = event.learnStyle

                viewModelScope.launch {
                    settingsUseCases.changeSettings(newSettings)
                    getSettings()
                }
            }
        }
    }

    private fun getSettings(){
        getSettingsJob?.cancel()

        getSettingsJob = settingsUseCases.getSettings()
            .onEach { settings ->
                _settingsState.value = settingsState.value.copy(
                    settings = settings,
                    currentLearnStyle = settings.learnStyle
                )
            }
            .launchIn(viewModelScope)
    }

}