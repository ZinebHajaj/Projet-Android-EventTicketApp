package com.dcc.eventticketapp.ui.organizer

import com.dcc.eventticketapp.data.Entities.EventModel

data class OrganizerViewState(
    // État général
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,

    // Liste des événements
    val myEvents: List<EventModel> = emptyList(),

    // Événement sélectionné (pour édition)
    val selectedEvent: EventModel? = null,

    // Champs du formulaire
    val formTitle: String = "",
    val formCity: String = "",
    val formDate: String = "",
    val formImageUrl: String = "",
    val formPrice: String = "",
    val formCategory: String = "Concerts",

    // Validation
    val isFormValid: Boolean = false
)