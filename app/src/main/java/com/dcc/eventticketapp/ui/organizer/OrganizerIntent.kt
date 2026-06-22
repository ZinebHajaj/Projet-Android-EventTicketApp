package com.dcc.eventticketapp.ui.organizer

import com.dcc.eventticketapp.data.Entities.EventModel

sealed class OrganizerIntent {
    // Chargement
    object LoadMyEvents : OrganizerIntent()

    // CRUD
    data class CreateEvent(val event: EventModel) : OrganizerIntent()
    data class UpdateEvent(val event: EventModel) : OrganizerIntent()
    data class DeleteEvent(val eventId: String) : OrganizerIntent()

    // Sélection
    data class SelectEvent(val event: EventModel) : OrganizerIntent()
    object ClearSelection : OrganizerIntent()

    // Champs du formulaire
    data class TitleChanged(val title: String) : OrganizerIntent()
    data class CityChanged(val city: String) : OrganizerIntent()
    data class DateChanged(val date: String) : OrganizerIntent()
    data class ImageUrlChanged(val url: String) : OrganizerIntent()
    data class PriceChanged(val price: String) : OrganizerIntent()
    data class CategoryChanged(val category: String) : OrganizerIntent()

    object ResetForm : OrganizerIntent()
}