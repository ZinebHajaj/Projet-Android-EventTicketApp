package com.dcc.eventticketapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.Entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ProfileViewState())
    val state: StateFlow<ProfileViewState> = _state

    init { handleIntent(ProfileIntent.LoadProfile) }

    fun handleIntent(intent: ProfileIntent) {
        when (intent) {

            // Charger le profil (données simulées pour l'instant)
            is ProfileIntent.LoadProfile -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                    // Données simulées — à remplacer par Firebase plus tard
                    val fakeUser = User(
                        userId   = "001",
                        fullName = "El Mellas Manar",
                        email    = "manar@ticketgo.ma",
                        phone    = "+212 6XX XXX XXX"
                    )
                    _state.value = _state.value.copy(
                        isLoading         = false,
                        user              = fakeUser,
                        editName          = fakeUser.fullName,
                        editEmail         = fakeUser.email,
                        editPhone         = fakeUser.phone,
                        reservationsCount = 12,
                        favoritesCount    = 5,
                        eventsCount       = 3
                    )
                }
            }

            // Champs édition
            is ProfileIntent.NameChanged -> {
                _state.value = _state.value.copy(editName = intent.name)
            }
            is ProfileIntent.EmailChanged -> {
                _state.value = _state.value.copy(editEmail = intent.email)
            }
            is ProfileIntent.PhoneChanged -> {
                _state.value = _state.value.copy(editPhone = intent.phone)
            }

            // Sauvegarder les modifications
            is ProfileIntent.SubmitEditProfile -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true)
                    val updatedUser = User(
                        userId   = _state.value.user?.userId ?: "",
                        fullName = _state.value.editName,
                        email    = _state.value.editEmail,
                        phone    = _state.value.editPhone
                    )
                    _state.value = _state.value.copy(
                        isLoading      = false,
                        user           = updatedUser,
                        isSuccess      = true,
                        showEditDialog = false
                    )
                }
            }

            // Toggle dark mode
            is ProfileIntent.ToggleDarkMode -> {
                _state.value = _state.value.copy(
                    isDarkMode = !_state.value.isDarkMode
                )
            }

            // Toggle notifications
            is ProfileIntent.ToggleNotifications -> {
                _state.value = _state.value.copy(
                    notificationsEnabled = !_state.value.notificationsEnabled
                )
            }

            // Déconnexion
            is ProfileIntent.Logout -> {
                _state.value = _state.value.copy(isLoggedOut = true)
            }

            // Reset
            is ProfileIntent.ResetState -> {
                _state.value = ProfileViewState()
            }
        }
    }

    fun showEditDialog() {
        _state.value = _state.value.copy(showEditDialog = true)
    }

    fun hideEditDialog() {
        _state.value = _state.value.copy(showEditDialog = false)
    }

    fun showLogoutDialog() {
        _state.value = _state.value.copy(showLogoutDialog = true)
    }

    fun hideLogoutDialog() {
        _state.value = _state.value.copy(showLogoutDialog = false)
    }
}