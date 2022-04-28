package neet.code.wearable.feature_deck.presentation

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import neet.code.wearable.MainActivity
import neet.code.wearable.feature_deck.domain.use_case.DecksUseCases


@HiltViewModel
class DecksViewModel @Inject constructor(
    private val deckUseCases: DecksUseCases):
    ViewModel()
{

    private val _deckState = mutableStateOf(DecksState())
    val deckState: State<DecksState> = _deckState

    private val _decks = mutableStateListOf<String>()
    val decks: List<String> = _decks

    private var getDecksJob: Job? = null

    init{
        getDecks()
    }

    fun onEvent(event: DecksEvent){
        when (event) {
            is DecksEvent.ChangeLearnStyle -> {
                _deckState.value = deckState.value.copy(
                    learnStyle = event.learnStyle
                )
            }
        }
    }

    private fun getDecks(){
        getDecksJob?.cancel()
        getDecksJob = deckUseCases.getDecks()
            .onEach { decks ->
                _deckState.value = deckState.value.copy(
                    decks = decks
                )
            }
            .launchIn(viewModelScope)
    }
}
