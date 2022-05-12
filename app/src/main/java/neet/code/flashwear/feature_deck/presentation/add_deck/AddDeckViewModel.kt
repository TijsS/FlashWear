package neet.code.flashwear.feature_deck.presentation.add_deck

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import neet.code.flashwear.R
import neet.code.flashwear.feature_deck.domain.model.Deck
import neet.code.flashwear.feature_deck.domain.model.InvalidDeckException
import neet.code.flashwear.feature_deck.domain.use_case.DecksUseCases
import javax.inject.Inject

@HiltViewModel
class AddDeckViewModel @Inject constructor(
    private val deckUseCases: DecksUseCases
    ): ViewModel()
{
    private val _state = mutableStateOf(AddDecksState())
    val state: State<AddDecksState> = _state

    private val _deck = mutableStateOf(AddDecksState())
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
            }
            is AddDeckEvent.AddDeck -> {
                viewModelScope.launch {
                    try {
                        deckUseCases.addDeck(
                            Deck(
                                name = deck.value.text,
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveDeck)

                    } catch(e: InvalidDeckException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message?.toInt() ?: R.string.deck_general_error
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: Int): UiEvent()
        object SaveDeck: UiEvent()
    }
}