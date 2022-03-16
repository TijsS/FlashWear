package neet.code.flashwear.feature_deck.presentation.add_deck

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.model.InvalidDeckException
import neet.code.flashwear.feature_deck.domain.use_case.DecksUseCases
import neet.code.flashwear.feature_deck.domain.util.DeckOrder
import neet.code.flashwear.feature_deck.domain.util.OrderType
import neet.code.flashwear.feature_deck.presentation.decks.DecksEvent
import neet.code.flashwear.feature_deck.presentation.decks.DecksState
import javax.inject.Inject

@HiltViewModel
class AddDeckViewModel @Inject constructor(
    private val deckUseCases: DecksUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = mutableStateOf(AddDecksState())
    val state: State<AddDecksState> = _state

    private val _deck = mutableStateOf(AddDecksState(
        hint = "Enter title..."
    ))
    val deck: State<AddDecksState> = _deck

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
    }

    fun onEvent(event: AddDeckEvent){
        when (event) {
            is AddDeckEvent.EnteredName -> {
                _deck.value = deck.value.copy(
                    text = event.value
                )
                Log.i("test", "onEvent: EnteredName, ${event.value}")
            }
            is AddDeckEvent.AddDeck -> {
                viewModelScope.launch {
                    try {
                        deckUseCases.addDeck(
                            Deck(
                                name = deck.value.text,
                                questions = "none",
                                statistics = "none",
                                created = System.currentTimeMillis(),
                                lastPlayed = System.currentTimeMillis(),
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveDeck)
                    } catch(e: InvalidDeckException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save deck"
                            )
                        )
                    }
                }

            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveDeck: UiEvent()
    }
}