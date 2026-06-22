package com.dcc.eventticketapp.ui.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.Repository.AuthRepository
import com.dcc.eventticketapp.data.Repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val repository: TicketRepository,
    private val authRepository : AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TicketViewState())
    val state: StateFlow<TicketViewState> = _state

    fun handleIntent(intent: TicketIntent) {
        when (intent) {
            is TicketIntent.LoadTickets -> {
                viewModelScope.launch { loadTickets() }
            }
            is TicketIntent.SelectTicket -> {
                val ticket = _state.value.tickets.find { it.ticketId == intent.ticketId }
                _state.value = _state.value.copy(selectedTicket = ticket)
            }
            is TicketIntent.DismissTicket -> {
                _state.value = _state.value.copy(selectedTicket = null)
            }
        }
    }

    private suspend fun loadTickets() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val userId = authRepository.currentUserId()
                ?: throw Exception("Utilisateur non connecté")

            val tickets = repository.getTickets(userId)
            _state.value = TicketViewState(isLoading = false, tickets = tickets)
        } catch (e: Exception) {
            _state.value = TicketViewState(
                isLoading = false,
                error     = e.message ?: "Erreur de chargement"
            )
        }
    }
}