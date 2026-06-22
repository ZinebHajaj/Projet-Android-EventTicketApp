package com.dcc.eventticketapp.ui.organizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.data.Repository.AuthRepository
import com.dcc.eventticketapp.data.Repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class OrganizerViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrganizerViewState())
    val state: StateFlow<OrganizerViewState> = _state

    init {
        handleIntent(OrganizerIntent.LoadMyEvents)
    }

    fun handleIntent(intent: OrganizerIntent) {
        when (intent) {
            is OrganizerIntent.LoadMyEvents -> {
                viewModelScope.launch { loadMyEvents() }
            }

            is OrganizerIntent.CreateEvent -> {
                viewModelScope.launch { createEvent(intent.event) }
            }

            is OrganizerIntent.UpdateEvent -> {
                viewModelScope.launch { updateEvent(intent.event) }
            }

            is OrganizerIntent.DeleteEvent -> {
                viewModelScope.launch { deleteEvent(intent.eventId) }
            }

            is OrganizerIntent.SelectEvent -> {
                _state.value = _state.value.copy(
                    selectedEvent = intent.event,
                    formTitle = intent.event.title,
                    formCity = intent.event.city,
                    formDate = intent.event.date,
                    formImageUrl = intent.event.imageUrl,
                    formPrice = intent.event.priceStandard.toString(),
                    formCategory = intent.event.category
                )
                validateForm()
            }

            is OrganizerIntent.ClearSelection -> {
                _state.value = _state.value.copy(selectedEvent = null)
                handleIntent(OrganizerIntent.ResetForm)
            }

            // Champs du formulaire
            is OrganizerIntent.TitleChanged -> {
                _state.value = _state.value.copy(formTitle = intent.title)
                validateForm()
            }
            is OrganizerIntent.CityChanged -> {
                _state.value = _state.value.copy(formCity = intent.city)
                validateForm()
            }
            is OrganizerIntent.DateChanged -> {
                _state.value = _state.value.copy(formDate = intent.date)
                validateForm()
            }
            is OrganizerIntent.ImageUrlChanged -> {
                _state.value = _state.value.copy(formImageUrl = intent.url)
                validateForm()
            }
            is OrganizerIntent.PriceChanged -> {
                _state.value = _state.value.copy(formPrice = intent.price)
                validateForm()
            }
            is OrganizerIntent.CategoryChanged -> {
                _state.value = _state.value.copy(formCategory = intent.category)
                validateForm()
            }

            is OrganizerIntent.ResetForm -> {
                _state.value = _state.value.copy(
                    formTitle = "",
                    formCity = "",
                    formDate = "",
                    formImageUrl = "",
                    formPrice = "",
                    formCategory = "Concerts",
                    selectedEvent = null,
                    error = null,
                    successMessage = null
                )
            }
        }
    }

    private suspend fun loadMyEvents() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val organizerId = authRepository.currentUserId() ?: throw Exception("Non connecté")
            val events = eventRepository.getMyEvents(organizerId)
            _state.value = _state.value.copy(
                isLoading = false,
                myEvents = events
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = e.message ?: "Erreur de chargement"
            )
        }
    }

    private suspend fun createEvent(event: EventModel) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val success = eventRepository.createEvent(event)
            if (success) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    successMessage = "Événement créé avec succès"
                )
                handleIntent(OrganizerIntent.ResetForm)
                handleIntent(OrganizerIntent.LoadMyEvents)
            } else {
                throw Exception("Échec de la création")
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = e.message ?: "Erreur de création"
            )
        }
    }

    private suspend fun updateEvent(event: EventModel) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val success = eventRepository.updateEvent(event)
            if (success) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    successMessage = "Événement mis à jour"
                )
                handleIntent(OrganizerIntent.ClearSelection)
                handleIntent(OrganizerIntent.LoadMyEvents)
            } else {
                throw Exception("Échec de la mise à jour")
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = e.message ?: "Erreur de mise à jour"
            )
        }
    }

    private suspend fun deleteEvent(eventId: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val success = eventRepository.deleteEvent(eventId)
            if (success) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    successMessage = "Événement supprimé"
                )
                handleIntent(OrganizerIntent.LoadMyEvents)
            } else {
                throw Exception("Échec de la suppression")
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = e.message ?: "Erreur de suppression"
            )
        }
    }

    private fun validateForm() {
        val current = _state.value
        val isValid = current.formTitle.isNotBlank() &&
                current.formCity.isNotBlank() &&
                current.formDate.isNotBlank() &&
                current.formPrice.toDoubleOrNull() != null
        _state.value = _state.value.copy(isFormValid = isValid)
    }

    // PAS suspend - retourne directement l'EventModel
    fun buildEventFromForm(): EventModel {
        val current = _state.value
        return EventModel(
            id = current.selectedEvent?.id ?: UUID.randomUUID().toString(),
            title = current.formTitle,
            city = current.formCity,
            date = current.formDate,
            imageUrl = current.formImageUrl.ifBlank {
                "https://picsum.photos/seed/${current.formTitle.hashCode()}/400/300"
            },
            priceStandard = current.formPrice.toDoubleOrNull() ?: 0.0,
            category = current.formCategory,
            organizerId = authRepository.currentUserId() ?: "",
            createdAt = current.selectedEvent?.createdAt ?: System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }
}