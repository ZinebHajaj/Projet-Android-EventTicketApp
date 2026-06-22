package com.dcc.eventticketapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.Entities.User
import com.dcc.eventticketapp.data.Repository.AuthRepository
import com.dcc.eventticketapp.data.Repository.FavoritesRepository
import com.dcc.eventticketapp.data.Repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val favoritesRepository: FavoritesRepository,
    private val ticketRepository  : TicketRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileViewState())
    val state: StateFlow<ProfileViewState> = _state

    init { handleIntent(ProfileIntent.LoadProfile) }

    fun handleIntent(intent: ProfileIntent) {
        when (intent) {

            is ProfileIntent.LoadProfile -> {
                viewModelScope.launch {

                    if (_state.value.user != null) return@launch

                    _state.value = _state.value.copy(isLoading = true)


                    val user = authRepository.getCurrentUser()
                    // Favoris réels depuis Firestore
                    val favoritesCount = try {
                        favoritesRepository.getFavorites().size
                    } catch (e: Exception) { 0 }
                    // Tickets (mockés pour l'instant)
                    val reservationsCount = try {
                        val userId = authRepository.currentUserId() ?: ""
                        ticketRepository.getTickets(userId).size
                    } catch (e: Exception) { 0 }

                    _state.value = _state.value.copy(
                        isLoading = false,
                        user = user,
                        isAuthenticated = user != null,
                        editName = user?.fullName ?: "",
                        editEmail = user?.email ?: "",
                        editPhone = user?.phone ?: "",
                        favoritesCount = favoritesCount,
                        reservationsCount = reservationsCount
                    )
                }
            }
            is ProfileIntent.NavigateTo -> {
                _state.value = _state.value.copy(navigateTo = intent.destination)
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
            // Nouvelle photo de profil sélectionnée
            is ProfileIntent.PhotoChanged -> {
                _state.value = _state.value.copy(photoUri = intent.uri)
            }

            // Sauvegarder les modifications
            is ProfileIntent.SubmitEditProfile -> {

                viewModelScope.launch {

                    try {

                        _state.value = _state.value.copy(
                            isLoading = true,
                            error = null
                        )

                        val updatedUser =
                            authRepository.updateProfile(
                                fullName = _state.value.editName,
                                phone = _state.value.editPhone
                            )

                        _state.value = _state.value.copy(
                            isLoading = false,
                            user = updatedUser,
                            isSuccess = true,
                            showEditDialog = false
                        )

                    } catch (e: Exception) {

                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
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

                authRepository.logout()

                _state.value = _state.value.copy(
                    user = null,
                    isAuthenticated = false,
                    isLoggedOut = true
                )
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

    fun clearNavigation() {
        _state.value = _state.value.copy(navigateTo = null)
    }
}