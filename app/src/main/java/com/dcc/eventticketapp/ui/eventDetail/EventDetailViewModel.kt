package com.dcc.eventticketapp.ui.eventDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.Repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventRepository
)  : ViewModel() {

    private val _state = MutableStateFlow(EventDetailViewState())
    val state: StateFlow<EventDetailViewState> = _state

    fun handleIntent(intent: EventDetailIntent) {
        when (intent) {
            is EventDetailIntent.LoadEvent -> {
                viewModelScope.launch { loadEvent(intent.eventId) }
            }
            is EventDetailIntent.ToggleFavorite -> {
                val current = _state.value.event ?: return
                _state.value = _state.value.copy(
                    event = current.copy(isFavorite = !current.isFavorite)
                )
            }
            is EventDetailIntent.BookEvent -> {
                _state.value = _state.value.copy(isBooked = true)
            }
        }
    }

    private suspend fun loadEvent(eventId: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val event = repository.getEventById(eventId)
            _state.value = EventDetailViewState(isLoading = false, event = event)
        } catch (e: Exception) {
            _state.value = EventDetailViewState(
                isLoading = false,
                error     = e.message ?: "Erreur de chargement"
            )
        }
    }
}