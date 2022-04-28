package neet.code.flashwear.feature_deck.presentation.decks

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import neet.code.flashwear.feature_deck.domain.use_case.DecksUseCases
import neet.code.flashwear.feature_deck.domain.util.DeckOrder
import neet.code.flashwear.feature_deck.domain.util.OrderType
import neet.code.flashwear.feature_deck.presentation.add_deck.AddDeckViewModel
import neet.code.flashwear.feature_question.domain.use_case.QuestionsUseCases
import neet.code.flashwear.feature_wearable.WearableUseCases
import javax.inject.Inject

@HiltViewModel
class DeckViewModel @Inject constructor(
    private val deckUseCases: DecksUseCases,
    private val questionsUseCases: QuestionsUseCases,
    private val wearableUseCases: WearableUseCases,
): ViewModel() {

    private val _deckState = mutableStateOf(DecksState())
    val deckState: State<DecksState> = _deckState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getDecksJob: Job? = null


    init{
        getDecks(DeckOrder.LastPlayed(OrderType.Descending))
    }

    fun onEvent(event: DecksEvent){
        when (event) {
             is DecksEvent.Order -> {
                 if (deckState.value.deckOrder::class == event.deckOrder::class &&
                     deckState.value.deckOrder.orderType == event.deckOrder.orderType
                 ){
                     return
                 }
                 getDecks(event.deckOrder)
             }

            is DecksEvent.ToggleOrderSection -> {
                _deckState.value = deckState.value.copy(
                    isOrderSectionVisible = !deckState.value.isOrderSectionVisible
                )
            }

            is DecksEvent.ToggleActionMenu -> {
                //toggle the isFloatingMenuVisible state value
                _deckState.value = _deckState.value.copy(
                    isFloatingMenuVisible = !deckState.value.isFloatingMenuVisible
                )
            }

            is DecksEvent.SyncWithWearable -> {
                viewModelScope.launch {
                    wearableUseCases.syncDecks(hardSync = true)

                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            "Synced"
                        )
                    )
                }
            }
        }
    }

    private fun getDecks(deckOrder: DeckOrder){
        getDecksJob?.cancel()
        getDecksJob = deckUseCases.getDecks(deckOrder)
            .onEach { decks ->

                //get the scores for each deck
                decks.forEach{ deck ->
                    val newAverageScoreOfDecks = deckState.value.averageScoreOfDecks

                    if (deck.id != null) {
                        newAverageScoreOfDecks[deck.id] = questionsUseCases.averageScoreOfDeck(deck.id)
                    }

                    _deckState.value = deckState.value.copy(
                        averageScoreOfDecks = newAverageScoreOfDecks
                    )
                }

                _deckState.value = deckState.value.copy(
                    decks = decks,
                    deckOrder = deckOrder
                )
            }
            .launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
    }
}